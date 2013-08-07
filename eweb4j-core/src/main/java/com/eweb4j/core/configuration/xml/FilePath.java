package com.eweb4j.core.configuration.xml;

import org.simpleframework.xml.Attribute;

public class FilePath {

	@Attribute
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "FilePath [path=" + path + "]";
	}

	
	
}
