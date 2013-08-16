package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class ConfigurationBean {

	@Attribute
	private String id;
	@Attribute(required=false)
	private String type;
	@ElementList(required=false)
	private List<PropertyBean> properties = new ArrayList<PropertyBean>();
	@ElementList(required=false)
	private List<FileBean> files = new ArrayList<FileBean>();
	@Attribute(required=false)
	private String holder;//容器处理类名
	
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
	public List<PropertyBean> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyBean> properties) {
		this.properties = properties;
	}
	public List<FileBean> getFiles() {
		return files;
	}
	public void setFiles(List<FileBean> files) {
		this.files = files;
	}
	
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", type=" + type + ", properties="
				+ properties + ", files=" + files + ", holder=" + holder + "]";
	}
	
}
