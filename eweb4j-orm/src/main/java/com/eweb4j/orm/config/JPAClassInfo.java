package com.eweb4j.orm.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity 类的JPA注解信息
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午02:36:46
 */
public class JPAClassInfo {
	
	/**
	 * 该Entity映射的数据库表名
	 */
	public String table;
	
	/**
	 * 该Entity的ID属性名
	 */
	public String id;
	
	/**
	 * 该Entity的ID字段名
	 */
	public String idCol;
	
	/**
	 * 该Entity属性的JAP注解信息
	 */
	public List<JPAFieldInfo> fieldInfos = new ArrayList<JPAFieldInfo>();
	
	public JPAFieldInfo getFieldInfoByColumn(String name){
		for (JPAFieldInfo jpa : fieldInfos){
			if (jpa.column.equals(name))
				return jpa;
		}
		
		return null;
	}
	
	public JPAFieldInfo getFieldInfoByField(String name){
		for (JPAFieldInfo jpa : fieldInfos){
			if (jpa.name.equals(name))
				return jpa;
		}
		
		return null;
	}
}
