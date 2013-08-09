package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class Configuration {

	@Attribute
	private String id;
	@Attribute(required=false)
	private String type;
	@ElementList(required=false)
	private List<Property> properties = new ArrayList<Property>();
	@ElementList(required=false)
	private List<FilePath> files = new ArrayList<FilePath>();
	@Attribute(required=false)
	private String clazz;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public List<FilePath> getFiles() {
		return files;
	}
	public void setFiles(List<FilePath> files) {
		this.files = files;
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", type=" + type + ", properties="
				+ properties + ", files=" + files + ", clazz=" + clazz + "]";
	}
	
}
