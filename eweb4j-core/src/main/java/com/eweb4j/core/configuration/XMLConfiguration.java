package com.eweb4j.core.configuration;

import java.util.Map;

public abstract class XMLConfiguration<T> extends AbstractConfiguration<String, T>{

	protected Map<String, T> values = null;
	protected Map<?,?> context = null;
	protected String xml = null;
	protected String xmlBeanClass = null;
	
	public void setContext(Map<?,?> context){
		this.context = context;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public void setXmlBeanClass(String xmlBeanClass){
		this.xmlBeanClass = xmlBeanClass;
	}
	
	public Map<String, T> getMap() {
		return this.values;
	}

}
