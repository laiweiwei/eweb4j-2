package com.eweb4j.core.ioc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.configuration.XMLConfiguration;
import com.eweb4j.core.configuration.xml.IOCXmlBean;
import com.eweb4j.core.configuration.xml.PojoXmlBean;

/**
 * IOC Configuration
 * @author vivi
 *
 * @param <T>
 */
public class IOCConfiguration extends XMLConfiguration<String, PojoXmlBean>{

	//IOC Pojo xml config cache
	protected Map<String, PojoXmlBean> pojoMap = new HashMap<String, PojoXmlBean>();;
	
	public IOCConfiguration(){
	}
	
	public IOCConfiguration(String xml){
		try {
			this.parseXml(xml, null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void parseXml(String xml, Map<?, ?> context) throws Throwable {
		//使用spring的simple-xml组件解析XML
		Serializer serializer = null;
		if (context == null)
			serializer = new Persister();
		else
			serializer = new Persister(context);
		
		File f = new File(xml);
		if (!f.exists()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" not found");
    	if (!f.isFile()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" is not a file");
    	
		IOCXmlBean ioc = serializer.read(IOCXmlBean.class, f);
		List<PojoXmlBean> pojos = ioc.getPojos();
		if (pojos == null || pojos.isEmpty()) return;
		for (PojoXmlBean pojo : pojos){
			pojoMap.put(pojo.getId(), pojo);
		}
	}
	
	@Override
	public PojoXmlBean get(String key, Object... args) {
		return this.pojoMap.get(key);
	}
	
	public Map<String, PojoXmlBean> getMap() {
		return this.pojoMap;
	}
	
}
