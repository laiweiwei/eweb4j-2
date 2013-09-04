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
	protected RowMappings rowMappings = null;
	protected ReflectUtil reflectUtil = null;
	protected Class<?> cls = null;
	protected Object pojo = null;
	protected String alias = "";
	protected HashMap<String, String> joins = new HashMap<String, String>();
	protected EntityRelationMapping erm = null;
	
	public SQL(){
		this.rowMappings = new RowMappings(this.cls);
	}
	
	public SQL(DataSource ds, Object pojo) {
		setDataSource(ds);
		setPojo(pojo);
		this.rowMappings = new RowMappings(this.cls);
	}
	
	public SQL(EWeb4J eweb4j, Object pojo){
		setPojo(pojo);
		setEWeb4J(eweb4j);
	}
	
	public void setEWeb4J(EWeb4J eweb4j){
		this.eweb4j = eweb4j;
		this.rowMappings = new RowMappings(this.cls, this.eweb4j.getConfigFactory());
		this.dataSource = this.eweb4j.getConfigFactory().getDefaultDataSource();
		this.erm = this.eweb4j.getConfigFactory().getEntityRelationMapping(this.cls.getName());
	}
	
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
	}
	
	public void setPojo(Object anything){
		if (Class.class.isInstance(anything)) {
			this.cls = (Class<?>) anything;
			if (!Map.class.isAssignableFrom(cls)) {
				this.reflectUtil = new ReflectUtil(this.cls);
				this.pojo = this.reflectUtil.getObject();
			}
		}else{
			this.pojo = anything;
			this.cls = this.pojo.getClass();
			if (!Map.class.isAssignableFrom(cls)) {
				this.reflectUtil = new ReflectUtil(this.pojo);
			}
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
		
		String sql = fmtSql(this.cls, reflectUtil, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		List<T> list = this.rowMappings.mapping(rows);
		return list == null ? null : list.isEmpty() ? null : list.get(0);
	}
	
	public <T> List<T> query(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		String sql = fmtSql(this.cls, reflectUtil, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		return this.rowMappings.mapping(rows);
	}

	public Number update(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		
		String sql = fmtSql(this.cls, reflectUtil, alias, joins, eql, args);
		return JDBCHelper.execute(dataSource, sql, args.toArray());
	}

	private String fmtSql(Class<?> cls, ReflectUtil ru, String alias, Map<String, String> joins, String _sql, List<Object> args) {
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

		EntityRelationMapping erm = null;
		if (this.eweb4j != null)
			erm = this.eweb4j.getConfigFactory().getEntityRelationMapping(cls.getName());
		if (erm == null) return sql;
		
		sql = sql.replace("#" + alias + "table", erm.getTable()).replace("#" + alias + "id", alias + erm.getIdCol());

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> _args = new ArrayList<Object>();
		
		for (FieldRelationMapping frm : erm.getFieldRelationMappings()) {
			sql = sql.replace("#" + alias + frm.getName(), alias + frm.getColumn());
			
			if (sql.contains("#columns") && sql.contains("#values")) {
				if (frm.getName().equals(erm.getId())) continue;
				try {
					Method getter = this.reflectUtil.getGetter(frm.getName());
					Object val = getter.invoke(this.reflectUtil.getObject());
					if (val == null) continue;
					String dataType = frm.getDataType();
					if (JoinType.ONE_2_ONE.toString().equalsIgnoreCase(dataType) || JoinType.MANY_2_ONE.toString().equalsIgnoreCase(dataType)) {
						String relCol = frm.getRelCol();
						Field field = ru.getField(frm.getName());
						Class<?> tarClass = field.getType();
						EntityRelationMapping tarJpa = null;
						if (this.eweb4j != null)
							tarJpa = this.eweb4j.getConfigFactory().getEntityRelationMapping(tarClass);
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
					
					columns.append(frm.getColumn());
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
