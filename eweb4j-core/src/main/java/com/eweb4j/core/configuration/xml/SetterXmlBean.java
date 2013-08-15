package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class SetterXmlBean {

	/**
	 * setter方法名，不包括 set,比如 setXxx 那么 name 就是 xxx
	 */
	@Attribute
	private String name;
	/**
	 * 引用其他pojo的id，若refer为空，就取 type 和 value
	 */
	@Attribute(required=false)
	private String refer;
	
	/**
	 * 基本数据类型的类路径或缩写，比如 int,string
	 */
	@Attribute(required=false)
	private String type;
	
	/**
	 * 基本数据值
	 */
	@Attribute(required=false)
	private String value;
	
	public String getRefer() {
		return refer;
	}
	public void setRefer(String refer) {
		this.refer = refer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Setter [refer=" + refer + ", name=" + name + ", type=" + type
				+ ", value=" + value + "]";
	}
	
}
