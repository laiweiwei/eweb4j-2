package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Configuration {

	@Attribute
	private String id;
	@Attribute(required=false)
	private String type;
	@ElementList(required=false)
	private List<Property> properties = new ArrayList<Property>();
	@Element(required=false)
	private FilePath file;
	
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
	
	public FilePath getFile() {
		return file;
	}
	public void setFile(FilePath file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", type=" + type + ", properties="
				+ properties + ", file=" + file + "]";
	}
	
}
