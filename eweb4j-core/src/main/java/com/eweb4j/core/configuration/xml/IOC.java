package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class IOC {
	
	@ElementList
	private List<com.eweb4j.core.configuration.xml.Pojo> pojos = new ArrayList<com.eweb4j.core.configuration.xml.Pojo>();

	public List<com.eweb4j.core.configuration.xml.Pojo> getPojos() {
		return pojos;
	}

	public void setPojos(List<com.eweb4j.core.configuration.xml.Pojo> pojos) {
		this.pojos = pojos;
	}

	@Override
	public String toString() {
		return "IOC [pojos=" + pojos + "]";
	}
	
}
