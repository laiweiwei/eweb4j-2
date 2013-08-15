package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class FileXmlBean {

	@Attribute
	private String path;
	@Attribute(required=false)
	private int enabled = 1;
	@Attribute(required=false)
	private String clazz;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
		return "FilePath [path=" + path + ", enabled=" + enabled + ", clazz="
				+ clazz + "]";
	}

}
