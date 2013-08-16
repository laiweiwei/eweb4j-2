package com.eweb4j.jpa.config;

import com.eweb4j.core.orm.FieldRelationMapping;

public class JPAFieldRelationMapping implements FieldRelationMapping{

	private String name;
	private String column;
	private String dataType;
	private Class<?> joinEntity;
	private String relCol;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Class<?> getJoinEntity() {
		return joinEntity;
	}
	public void setJoinEntity(Class<?> joinEntity) {
		this.joinEntity = joinEntity;
	}
	public String getRelCol() {
		return relCol;
	}
	public void setRelCol(String relCol) {
		this.relCol = relCol;
	}
	
}
