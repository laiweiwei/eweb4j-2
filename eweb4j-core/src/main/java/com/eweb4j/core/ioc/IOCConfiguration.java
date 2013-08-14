package com.eweb4j.core.ioc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.configuration.XMLConfiguration;
import com.eweb4j.core.configuration.xml.IOC;
import com.eweb4j.core.configuration.xml.Pojo;

/**
 * IOC Configuration
 * @author vivi
 *
 * @param <T>
 */
public class IOCConfiguration extends XMLConfiguration<String, Pojo>{

	//IOC Pojo xml config cache
	protected Map<String, Pojo> pojoMap = null;
	protected String xml = null;
	protected Map<?, ?> context = null;
	
	public IOCConfiguration(){
	}
	
	public IOCConfiguration(String xml){
		this.xml = xml;
		try {
			this.parseXml();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void parseXml() throws Throwable {
		//使用spring的simple-xml组件解析XML
		Serializer serializer = null;
		if (context == null)
			serializer = new Persister();
		else
			serializer = new Persister(context);
		
		File f = new File(xml);
		if (!f.exists()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" not found");
    	if (!f.isFile()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" is not a file");
    	
		IOC ioc = serializer.read(IOC.class, f);
		List<Pojo> pojos = ioc.getPojos();
		if (pojos == null || pojos.isEmpty()) return;
		this.pojoMap = new HashMap<String, Pojo>(pojos.size());
		for (Pojo pojo : pojos){
			pojoMap.put(pojo.getId(), pojo);
		}
	}
	
	@Override
	public Pojo get(String key, Object... args) {
		return this.pojoMap.get(key);
	}
	
	public Map<String, Pojo> getMap() {
		return this.pojoMap;
	}

	public void setXml(String xml){
		this.xml = xml;
	}
	
	public void setContext(Map<?, ?> context){
		this.context = context;
	}
	
}
