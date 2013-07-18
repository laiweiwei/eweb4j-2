package com.eweb4j.orm.config;


/**
 * Entity 类属性的JPA注解信息
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午02:16:06
 */
public class JPAFieldInfo {

	/**
	 * 属性名称
	 */
	public String name;
	
	/**
	 * 映射的字段名
	 */
	public String column;
	
	/**
	 * 数据类型
	 */
	public String dataType;
	
	/**
	 * 若该属性是关联属性，关联到哪个Entity
	 */
	public Class<?> joinEntity;
	
	/**
	 * 若该属性是关联属性，关联到目标Entity的哪个column
	 */
	public String relCol;
	
}
