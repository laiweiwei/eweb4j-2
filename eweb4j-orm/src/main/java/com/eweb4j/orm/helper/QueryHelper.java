package com.eweb4j.orm.helper;

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

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.jdbc.JDBCHelper;
import com.eweb4j.core.jdbc.JDBCRow;
import com.eweb4j.orm.PojoMappings;
import com.eweb4j.orm.config.JPAClassInfo;
import com.eweb4j.orm.config.JPAFieldInfo;
import com.eweb4j.orm.config.JoinType;
import com.eweb4j.utils.ReflectUtil;

public class QueryHelper<T> {
	
	private DataSource dataSource = null;
	private ConfigurationFactory configFactory = null;
	private PojoMappings<T> pojoMappings = null;
	private ReflectUtil ru = null;
	private T model = null;
	private Class<T> cls = null;
	private String alias = "";
	private HashMap<String, String> joins = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public QueryHelper(T model, ConfigurationFactory configFactory){
		this.configFactory = configFactory;
		this.cls = (Class<T>) model.getClass();
		this.pojoMappings = new PojoMappings<T>(cls, configFactory);
		this.dataSource = configFactory.getDefaultDataSource();
		
		this.model = model;
		this.ru = new ReflectUtil(this.model);
	}
	
	public QueryHelper(Class<T> cls, ConfigurationFactory configFactory){
		this.configFactory = configFactory;
		this.pojoMappings = new PojoMappings<T>(cls, configFactory);
		this.dataSource = configFactory.getDefaultDataSource();
		this.cls = cls;
		try {
			this.model = cls.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		
		this.ru = new ReflectUtil(this.model);
	}
	
	public QueryHelper<T> alias(String alias) {
		this.alias = alias;
		return this;
	}

	public QueryHelper<T> join(String field, String alias) {
		this.joins.put(field, alias);
		return this;
	}

	public T queryOne(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		
		String sql = fmtSql(cls, ru, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		List<T> list = this.pojoMappings.mapping(rows);
		return list == null ? null : list.isEmpty() ? null : list.get(0);
	}
	
	public List<T> query(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		String sql = fmtSql(cls, ru, alias, joins, eql, args);
		List<JDBCRow> rows = JDBCHelper.find(dataSource, sql, args.toArray());
		
		return this.pojoMappings.mapping(rows);
	}

	public Number update(String eql, Object... _args) {
		List<Object> args = new ArrayList<Object>();
		if (_args != null) args.addAll(Arrays.asList(_args));
		
		String sql = fmtSql(cls, ru, alias, joins, eql, args);
		return JDBCHelper.execute(dataSource, sql, args.toArray());
	}

	private String fmtSql(Class<?> cls, ReflectUtil ru, String alias, Map<String, String> joins, String _sql, List<Object> args) {
		JPAClassInfo jpa = configFactory.getJPA(cls.getName());
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

		sql = sql.replace("#" + alias + "table", jpa.table).replace("#" + alias + "id", alias + jpa.idCol);

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> _args = new ArrayList<Object>();
		for (JPAFieldInfo f : jpa.fieldInfos) {
			sql = sql.replace("#" + alias + f.name, alias + f.column);
			Method getter = this.ru.getGetter(f.name);
			if (sql.contains("#columns") && sql.contains("#values")) {
				if (f.name.equals(jpa.id)) continue;
				try {
					Object val = getter.invoke(this.ru.getObject());
					String dataType = f.dataType;
					if (JoinType.ONE_2_ONE.toString().equalsIgnoreCase(dataType) || JoinType.MANY_2_ONE.toString().equalsIgnoreCase(dataType)) {
						String relCol = f.relCol;
						Field field = ru.getField(f.name);
						Class<?> tarClass = field.getType();
						JPAClassInfo tarJpa = this.configFactory.getJPA(tarClass);
						String relField = null;
						if (relCol == null || relCol.trim().length() == 0){
							relCol = tarJpa.idCol;
							relField = tarJpa.id;
						} else {
							JPAFieldInfo tarFieldInfo = tarJpa.getFieldInfoByColumn(relCol);
							relField = tarFieldInfo.name;
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
					
					columns.append(f.column);
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
