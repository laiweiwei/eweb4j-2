package com.eweb4j.core.configuration;

import java.util.Map;

public abstract class XMLConfiguration<K, V> extends AbstractConfiguration<K, V>{

	protected Map<?,?> context = null;
	protected String xml = null;
	
	public void setXml(String xml){
		this.xml = xml;
	}
	
	public void setContext(Map<?, ?> context){
		this.context = context;
	}
	
	public abstract void parseXml() throws Throwable; 
	
}
