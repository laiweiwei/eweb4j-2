package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class Property {

	@Attribute
	private String name;
	@Attribute
	private String value;
	@Attribute(required=false)
	private int isArray;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIsArray() {
		return isArray;
	}

	public void setIsArray(int isArray) {
		this.isArray = isArray;
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", value=" + value + ", isArray="
				+ isArray + "]";
	}

}
