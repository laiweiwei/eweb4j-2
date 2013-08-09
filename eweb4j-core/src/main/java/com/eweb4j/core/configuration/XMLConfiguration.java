package com.eweb4j.core.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public abstract class XMLConfiguration<T> extends AbstractConfiguration<String, T>{

	protected Map<String, T> values = null;
	protected Map<?,?> context = null;
	protected String xml = null;
	protected String xmlBeanClass = null;
	
	public void setContext(Map<?,?> context){
		this.context = context;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public void setXmlBeanClass(String xmlBeanClass){
		this.xmlBeanClass = xmlBeanClass;
	}
	
	public abstract void handleXmlBean(Object xmlBean);
	
	public void init(){
		this.values = new HashMap<String, T>();
		//使用spring的simple-xml组件解析XML为POJO
		
		Serializer serializer = null;
		if (context == null)
			serializer = new Persister();
		else
			serializer = new Persister(context);
		
		try {
			File f = new File(xml);
			if (!f.exists()) throw new Exception("xml->"+f.getAbsolutePath()+" not found");
        	if (!f.isFile()) throw new Exception("xml->"+f.getAbsolutePath()+" is not a file");
			Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(this.xmlBeanClass); 
        	Object xmlBean = serializer.read(cls, f);
			if (xmlBean == null) return ;
			
			handleXmlBean(xmlBean);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, T> getMap() {
		return this.values;
	}

}
