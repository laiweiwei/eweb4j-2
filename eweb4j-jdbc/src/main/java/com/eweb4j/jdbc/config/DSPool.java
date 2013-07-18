package com.eweb4j.jdbc.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Data Source Pool
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午05:20:31
 */
public class DSPool {

	private final static List<DSBean> beans = new ArrayList<DSBean>();
	
	public final static DSBean first() {
		for (DSBean bean : beans) {
			return bean;
		}
		
		return null;
	}
	
	public final static DSBean get(String name) {
		for (DSBean bean : beans) {
			if (name.equals(bean.name))
				return bean;
		}
		
		return null;
	}
	
	public final static void add(String name, DataSource ds) {
		DSBean bean = new DSBean();
		bean.name = name;
		bean.ds = ds;
		
		beans.add(bean);
	}
	
	public final static void remove(String name){
		beans.remove(name);
	}
	
	public final static void clear(){
		beans.clear();
	}
	
}
