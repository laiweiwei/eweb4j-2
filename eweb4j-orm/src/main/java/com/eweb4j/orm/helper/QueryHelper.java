package com.eweb4j.orm.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eweb4j.core.jdbc.JDBCHelper;
import com.eweb4j.core.jdbc.JDBCRow;
import com.eweb4j.core.jdbc.DSPool;
import com.eweb4j.orm.PojoMappings;
import com.eweb4j.orm.config.JPAClassInfo;
import com.eweb4j.orm.config.JPAScanner;
import com.eweb4j.orm.config.JPAFieldInfo;
import com.eweb4j.utils.ReflectUtil;

public class QueryHelper<T> {
	
	private ReflectUtil ru = null;
	private Class<T> model = null;
	private String alias = "";
	private HashMap<String, String> joins = new HashMap<String, String>();

	public QueryHelper(Class<T> model){
		this.model = model;
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

	public T queryOne(String eql, Object... args) {
		String sql = fmtSql(model, ru, alias, joins, eql);
		List<JDBCRow> rows = JDBCHelper.find(DSPool.first().ds, sql, args);
		List<T> list = PojoMappings.mapping(rows, model);
		return list == null ? null : list.isEmpty() ? null : list.get(0);
	}
	
	public List<T> query(String eql, Object... args) {
		String sql = fmtSql(model, ru, alias, joins, eql);
		List<JDBCRow> rows = JDBCHelper.find(DSPool.first().ds, sql, args);
		return PojoMappings.mapping(rows, model);
	}

	public Number update(String eql, Object... args) {
		String sql = fmtSql(model, ru, alias, joins, eql);
		return JDBCHelper.execute(DSPool.first().ds, sql, args);
	}

	private static String fmtSql(Class<?> cls, ReflectUtil ru, String alias, Map<String, String> joins, String _sql) {
		JPAClassInfo jpa = JPAScanner.scan(cls);
		String sql = _sql;
		if (joins != null && !joins.isEmpty()) {
			for (Iterator<Entry<String, String>> it = joins.entrySet()
					.iterator(); it.hasNext();) {
				Entry<String, String> e = it.next();
				String field = e.getKey();
				String _alias = e.getValue();
				sql = fmtSql(ru.getField(field).getType(), null, _alias, null,
						sql);
			}
		}
		if (alias != null && alias.trim().length() > 0)
			alias = alias + ".";

		sql = sql.replace("#" + alias + "table", jpa.table).replace("#" + alias + "id", alias + jpa.idCol);

		for (JPAFieldInfo f : jpa.fieldInfos)
			sql = sql.replace("#" + alias + f.name, alias + f.column);

		return sql;
	}

}
