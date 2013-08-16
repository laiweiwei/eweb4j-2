package com.eweb4j.core.orm;

public interface FieldRelationMapping {

	/**
	 * 属性名称
	 */
	public String getName();
	
	/**
	 * 设置属性名称
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * 映射的字段名
	 */
	public String getColumn();
	
	/**
	 * 获取映射的字段名
	 * @param column
	 */
	public void setColumn(String column);
	
	/**
	 * 数据类型
	 */
	public String getDataType();
	
	/**
	 * 设置数据类型
	 * @param dataType
	 * @return
	 */
	public void setDataType(String dataType);
	
	/**
	 * 若该属性是关联属性，关联到哪个Entity
	 */
	public Class<?> getJoinEntity();
	
	/**
	 * 设置关联的Entity
	 * @param joinEntity
	 */
	public void setJoinEntity(Class<?> joinEntity);
	
	/**
	 * 若该属性是关联属性，关联到目标Entity的哪个column
	 */
	public String getRelCol();
	
	/**
	 * 设置关联的字段名
	 * @param relCol
	 */
	public void setRelCol(String relCol);
	
	
}
