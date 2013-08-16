package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class PluginBean {
	
	@Attribute(required = true)
	private String id = null;
	
	@Attribute(required = true)
	private String clazz = null;
	
	private int enabled = 1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "PluginBean [id=" + id + ", clazz=" + clazz + ", enabled="
				+ enabled + "]";
	}
	
}
