package com.eweb4j.core.configuration;

import java.util.Map;

public abstract class XMLConfiguration<K, V> extends AbstractConfiguration<K, V>{

	public abstract void parseXml(String xml, Map<?, ?> context) throws Throwable; 
	
}
