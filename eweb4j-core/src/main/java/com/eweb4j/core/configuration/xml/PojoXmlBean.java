package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class PojoXmlBean {

	@Attribute
	private String id;
	
	/**
	 * pojo的实例化类型
	 */
	@Attribute(required=false)
	private String scope;//prototype|singleton
	
	/**
	 * pojo的类名
	 */
	@Attribute
	private String clazz;
	
	@ElementList(required=false)
	private List<com.eweb4j.core.configuration.xml.SetterXmlBean> setters = new ArrayList<com.eweb4j.core.configuration.xml.SetterXmlBean>();
	
	@ElementList(required=false)
	private List<com.eweb4j.core.configuration.xml.ConstructorXmlBean> constructors = new ArrayList<com.eweb4j.core.configuration.xml.ConstructorXmlBean>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public List<com.eweb4j.core.configuration.xml.SetterXmlBean> getSetters() {
		return setters;
	}

	public void setSetters(List<com.eweb4j.core.configuration.xml.SetterXmlBean> setters) {
		this.setters = setters;
	}

	public List<com.eweb4j.core.configuration.xml.ConstructorXmlBean> getConstructors() {
		return constructors;
	}

	public void setConstructors(
			List<com.eweb4j.core.configuration.xml.ConstructorXmlBean> constructors) {
		this.constructors = constructors;
	}

	@Override
	public String toString() {
		return "Pojo [id=" + id + ", scope=" + scope + ", clazz=" + clazz
				+ ", setters=" + setters + ", constructors=" + constructors
				+ "]";
	}
}
