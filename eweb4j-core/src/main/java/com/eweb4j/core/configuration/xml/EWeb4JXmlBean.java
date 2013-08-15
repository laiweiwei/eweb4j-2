package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class EWeb4JXmlBean {

	@ElementList
	private List<com.eweb4j.core.configuration.xml.ConfigurationXmlBean> configurations = new ArrayList<com.eweb4j.core.configuration.xml.ConfigurationXmlBean>();

	public List<com.eweb4j.core.configuration.xml.ConfigurationXmlBean> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<com.eweb4j.core.configuration.xml.ConfigurationXmlBean> configurations) {
		this.configurations = configurations;
	}

	@Override
	public String toString() {
		return "EWeb4j [configurations=" + configurations + "]";
	}

}
