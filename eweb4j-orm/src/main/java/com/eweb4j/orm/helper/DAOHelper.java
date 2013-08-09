package com.eweb4j.orm.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.orm.config.JPAClassInfo;
import com.eweb4j.orm.config.JPAFieldInfo;

@Entity
public class DAOHelper {

	private SQLHelper queryHelper = null;
	private JPAClassInfo jpa = null;

	public <T> DAOHelper(ConfigurationFactory configFactory, Class<?> cls){
		this.queryHelper = new SQLHelper(configFactory, cls);
		this.jpa = this.queryHelper.getConfigFactory().getJPA(cls);
	}
	
	public DAOHelper(ConfigurationFactory configFactory, Object pojo){
		this.queryHelper = new SQLHelper(configFactory, pojo);
		this.jpa = this.queryHelper.getConfigFactory().getJPA(pojo.getClass());
	}
	
	private Object _get_id(){
		String idField = this.jpa.id;
		Method idGetter = this.queryHelper.getReflectUtil().getGetter(idField);
		if (idGetter == null) return null;
		
		try {
			return idGetter.invoke(this.queryHelper.getPojo());
		} catch (Throwable e){
			e.printStackTrace();
		}
		return null;
	}
	
	private int _execute_by_id(String eql){
		try {
			Object idVal = _get_id();
			Number row = this.queryHelper.update(eql, idVal);
			return row.intValue();
		} catch (Throwable e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void create(){
		final String eql = "INSERT INTO #table(#columns) values(#values)";
		
		Number idVal = this.queryHelper.update(eql);
		String idField = this.jpa.id;
		Method idSetter = this.queryHelper.getReflectUtil().getSetter(idField);
		if (idSetter == null)
			return ;
		try {
			idSetter.invoke(this.queryHelper.getPojo(), idVal.longValue());
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
		return this.queryHelper.update(eql).intValue();
	}
	
	public int updateFields(String... fields){
		final String fmt = "UPDATE #table set %s where #id = ?";
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<Object>(fields.length);
		for (String field : fields) {
			try {
				if (sb.length() > 0)
					sb.append(",");
				JPAFieldInfo f = jpa.getFieldInfoByField(field);
				sb.append(f.column).append("=?");
				Method fieldGetter = this.queryHelper.getReflectUtil().getGetter(field);
				Object value = fieldGetter.invoke(this);
				args.add(value);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		Object idVal = _get_id();
		args.add(idVal);
		
		final String eql = String.format(fmt, sb.toString());
		Number row = this.queryHelper.update(eql, args.toArray());
		
		return row.intValue();
	}
	
	
}
