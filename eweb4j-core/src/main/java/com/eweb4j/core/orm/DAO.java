package com.eweb4j.core.orm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.orm.FieldRelationMapping;

public class DAO extends SQL{

	public DAO(){
		super();
	}
	
	public DAO(EWeb4J eweb4j, Object anything){
		super(eweb4j, anything);
	}
	
	private Object _get_id(){
		String idField = super.jpa.getId();
		Method idGetter = super.reflectUtil.getGetter(idField);
		if (idGetter == null) return null;
		
		try {
			return idGetter.invoke(super.reflectUtil.getObject());
		} catch (Throwable e){
			e.printStackTrace();
		}
		return null;
	}
	
	private int _execute_by_id(String eql){
		try {
			Object idVal = _get_id();
			Number row = super.update(eql, idVal);
			return row.intValue();
		} catch (Throwable e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void create(){
		final String eql = "INSERT INTO #table(#columns) values(#values)";
		
		Number idVal = super.update(eql);
		String idField = this.jpa.getId();
		Method idSetter = super.reflectUtil.getSetter(idField);
		if (idSetter == null)
			return ;
		try {
			idSetter.invoke(super.reflectUtil.getObject(), idVal.longValue());
		} catch (Throwable e){
			e.printStackTrace();
			return ;
		}
	}
	
	public int update(){
		final String eql = "UPDATE #table set(#columns=values) where #id = ?";
		return _execute_by_id(eql);
	}
	
	public int delete(){
		final String eql = "DELETE FROM #table where #id = ?";
		return _execute_by_id(eql);
	}
	
	public int clear(){
		final String eql = "delete from #table";
		return super.update(eql).intValue();
	}
	
	public int updateFields(String... fields){
		final String fmt = "UPDATE #table set %s where #id = ?";
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<Object>(fields.length);
		for (String field : fields) {
			try {
				if (sb.length() > 0)
					sb.append(",");
				FieldRelationMapping f = jpa.getFieldRelationMappingByField(field);
				sb.append(f.getColumn()).append("=?");
				Method fieldGetter = super.reflectUtil.getGetter(field);
				Object value = fieldGetter.invoke(super.reflectUtil.getObject());
				args.add(value);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		Object idVal = _get_id();
		args.add(idVal);
		
		final String eql = String.format(fmt, sb.toString());
		Number row = super.update(eql, args.toArray());
		
		return row.intValue();
	}
	
}
