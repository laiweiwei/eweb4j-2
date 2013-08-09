package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class Property {

	@Attribute
	private String name;
	@Attribute
	private String value;
	@Attribute(required=false)
	private int enabled = 1;//是否开启
	@Attribute(required=false)
	private String clazz;

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

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", value=" + value + ", enabled="
				+ enabled + ", clazz=" + clazz + "]";
	}
	
}
