package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class EWeb4j {

	@ElementList
	private List<com.eweb4j.core.configuration.xml.Configuration> configurations = new ArrayList<com.eweb4j.core.configuration.xml.Configuration>();

	public List<com.eweb4j.core.configuration.xml.Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<com.eweb4j.core.configuration.xml.Configuration> configurations) {
		this.configurations = configurations;
	}

	@Override
	public String toString() {
		return "EWeb4J [configurations=" + configurations + "]";
	}


}
