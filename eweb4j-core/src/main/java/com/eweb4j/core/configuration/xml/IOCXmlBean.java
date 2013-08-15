package com.eweb4j.core.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class IOCXmlBean {
	
	@ElementList
	private List<com.eweb4j.core.configuration.xml.PojoXmlBean> pojos = new ArrayList<com.eweb4j.core.configuration.xml.PojoXmlBean>();

	public List<com.eweb4j.core.configuration.xml.PojoXmlBean> getPojos() {
		return pojos;
	}

	public void setPojos(List<com.eweb4j.core.configuration.xml.PojoXmlBean> pojos) {
		this.pojos = pojos;
	}

	@Override
	public String toString() {
		return "IOC [pojos=" + pojos + "]";
	}
	
}
