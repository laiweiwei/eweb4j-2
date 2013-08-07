package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class Property {

	@Attribute
	private String name;
	@Attribute
	private String value;

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

	@Override
	public String toString() {
		return "Property [name=" + name + ", value=" + value + "]";
	}
	
}
