package com.eweb4j.core.orm;

import java.util.List;

public interface EntityRelationMapping {

	/**
	 * 获取映射的表名
	 */
	public String getTable();
	
	/**
	 * 设置映射的表名
	 * @param table
	 */
	public void setTable(String table);
	
	/**
	 * 获取ID属性名
	 */
	public String getId();
	
	/**
	 * 设置ID属性名
	 * @return
	 */
	public void setId(String id);
	
	/**
	 * 获取ID字段名
	 */
	public String getIdCol();
	
	/**
	 * 设置ID字段名
	 * @param idCol
	 */
	public void setIdCol(String idCol);
	
	public List<FieldRelationMapping> getFieldRelationMappings();

	public void setFieldRelationMappings(List<FieldRelationMapping> fieldRelationMappings);
	
	public FieldRelationMapping getFieldRelationMappingByColumn(String name);
	
	public FieldRelationMapping getFieldRelationMappingByField(String name);
	
}
