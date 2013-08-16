package com.eweb4j.core.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.jdbc.JDBCHelper;
import com.eweb4j.core.jdbc.JDBCRow;
import com.eweb4j.core.orm.EntityRelationMapping;
import com.eweb4j.core.orm.FieldRelationMapping;
import com.eweb4j.core.util.ReflectUtil;

public class SQL{
	
	protected EWeb4J eweb4j = null;
	protected DataSource dataSource = null;
	protected PojoMappings pojoMappings = null;
	protected ReflectUtil reflectUtil = null;
	protected Class<?> cls = null;
	protected Object pojo = null;
	protected EntityRelationMapping jpa = null;
	protected String alias = "";
	protected HashMap<String, String> joins = new HashMap<String, String>();

	public SQL(){}
	
	public SQL(EWeb4J eweb4j, Object anything){
		setPojo(anything);
		setEWeb4J(eweb4j);
	}
	
	public void setEWeb4J(EWeb4J eweb4j){
		this.eweb4j = eweb4j;
		this.jpa = this.eweb4j.getConfigFactory().getEntityRelationMapping(this.cls.getName());
		this.pojoMappings = new PojoMappings(this.cls, this.eweb4j.getConfigFactory());
		this.dataSource = this.eweb4j.getConfigFactory().getDefaultDataSource();
	}
	
	public void setPojo(Object anything){
		if (Class.class.isInstance(anything)) {
			this.cls = (Class<?>) anything;
			this.reflectUtil = new ReflectUtil(this.cls);
			this.pojo = this.reflectUtil.getObject();
		}else{
			this.pojo = anything;
			this.reflectUtil = new ReflectUtil(this.pojo);
			this.cls = this.pojo.getClass();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPojo(){
		return (T) this.pojo;
	}
	
	public SQL alias(String alias) {
		this.alias = alias;
		return this;
	}

	public SQL join(String field, String alias) {
		this.joins.put(field, alias);
		return this;
	}

	public <T> T queryOne(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		
		String sql = fmtSql(pojo.getClass(), reflectUtil, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		List<T> list = this.pojoMappings.mapping(rows);
		return list == null ? null : list.isEmpty() ? null : list.get(0);
	}
	
	public <T> List<T> query(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		String sql = fmtSql(pojo.getClass(), reflectUtil, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		return this.pojoMappings.mapping(rows);
	}

	public Number update(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		
		String sql = fmtSql(pojo.getClass(), reflectUtil, alias, joins, eql, args);
		return JDBCHelper.execute(dataSource, sql, args.toArray());
	}

	private String fmtSql(Class<?> cls, ReflectUtil ru, String alias, Map<String, String> joins, String _sql, List<Object> args) {
		EntityRelationMapping jpa = this.eweb4j.getConfigFactory().getEntityRelationMapping(cls.getName());
		String sql = _sql;
		if (joins != null && !joins.isEmpty()) {
			for (Iterator<Entry<String, String>> it = joins.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> e = it.next();
				String field = e.getKey();
				String _alias = e.getValue();
				Class<?> subCls = ru.getField(field).getType();
				sql = fmtSql(subCls, new ReflectUtil(subCls), _alias, null, sql, args);
			}
		}
		if (alias != null && alias.trim().length() > 0)
			alias = alias + ".";

		sql = sql.replace("#" + alias + "table", jpa.getTable()).replace("#" + alias + "id", alias + jpa.getIdCol());

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> _args = new ArrayList<Object>();
		for (FieldRelationMapping f : jpa.getFieldRelationMappings()) {
			sql = sql.replace("#" + alias + f.getName(), alias + f.getColumn());
			
			if (sql.contains("#columns") && sql.contains("#values")) {
				if (f.getName().equals(jpa.getId())) continue;
				try {
					Method getter = this.reflectUtil.getGetter(f.getName());
					Object val = getter.invoke(this.reflectUtil.getObject());
					if (val == null) continue;
					String dataType = f.getDataType();
					if (JoinType.ONE_2_ONE.toString().equalsIgnoreCase(dataType) || JoinType.MANY_2_ONE.toString().equalsIgnoreCase(dataType)) {
						String relCol = f.getRelCol();
						Field field = ru.getField(f.getName());
						Class<?> tarClass = field.getType();
						EntityRelationMapping tarJpa = this.eweb4j.getConfigFactory().getEntityRelationMapping(tarClass);
						String relField = null;
						if (relCol == null || relCol.trim().length() == 0){
							relCol = tarJpa.getIdCol();
							relField = tarJpa.getId();
						} else {
							FieldRelationMapping tarFieldInfo = tarJpa.getFieldRelationMappingByColumn(relCol);
							relField = tarFieldInfo.getName();
						}
						if (val != null) {
							ReflectUtil tarRu = new ReflectUtil(val);
							Method tarGetter = tarRu.getGetter(relField);
							val = tarGetter.invoke(val);
						}
					}
					
					if (columns.length() > 0) {
						columns.append(",");
					}
					if (values.length() > 0)
						values.append(",");
					
					columns.append(f.getColumn());
					values.append("?");
					
					_args.add(val);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		
		if (columns.length() > 0 && values.length() > 0){
			//#columns, #values
			sql = sql.replace("#columns", columns.toString()).replace("#values", values.toString());
			args.addAll(_args);
		}
		
		return sql;
	}

}
