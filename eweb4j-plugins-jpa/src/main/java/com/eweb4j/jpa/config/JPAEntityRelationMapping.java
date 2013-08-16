package com.eweb4j.jpa.config;

import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.orm.EntityRelationMapping;
import com.eweb4j.core.orm.FieldRelationMapping;

public class JPAEntityRelationMapping implements EntityRelationMapping{

	private List<FieldRelationMapping> fieldRelationMappings = new ArrayList<FieldRelationMapping>();
	
	private String table;
	private String id;
	private String idCol;
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdCol() {
		return idCol;
	}

	public void setIdCol(String idCol) {
		this.idCol = idCol;
	}

	public FieldRelationMapping getFieldRelationMappingByColumn(String name) {
		for (FieldRelationMapping jpa : getFieldRelationMappings()){
			if (jpa.getColumn().equals(name))
				return jpa;
		}
		
		return null;
	}

	public FieldRelationMapping getFieldRelationMappingByField(String name) {
		for (FieldRelationMapping jpa : getFieldRelationMappings()){
			if (jpa.getName().equals(name))
				return jpa;
		}
		
		return null;
	}

	public List<FieldRelationMapping> getFieldRelationMappings() {
		return fieldRelationMappings;
	}

	public void setFieldRelationMappings(List<FieldRelationMapping> fieldRelationMappings) {
		this.fieldRelationMappings = fieldRelationMappings;
	}

}
