package com.eweb4j.core.ioc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.XMLConfiguration;
import com.eweb4j.core.configuration.xml.IOCBean;
import com.eweb4j.core.configuration.xml.PojoBean;

/**
 * IOC Configuration
 * @author vivi
 *
 * @param <T>
 */
public class IOCConfiguration extends XMLConfiguration<String, PojoBean>{

	//IOC Pojo xml config cache
	protected Map<String, PojoBean> pojoMap = new HashMap<String, PojoBean>();;
	
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
	public void parseXml(final String _xml, Map<?, ?> context) throws Throwable {
		if (_xml == null) throw new RuntimeException("xml can not be null");
		
		//使用spring的simple-xml组件解析XML
		Serializer serializer = null;
		if (context == null)
			serializer = new Persister();
		else
			serializer = new Persister(context);
		
		String xml = EWeb4J.Constants.resolve_path(_xml);
		
		File f = new File(xml);
		if (!f.exists()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" not found");
    	if (!f.isFile()) throw new RuntimeException("xml->"+f.getAbsolutePath()+" is not a file");
    	
		IOCBean ioc = serializer.read(IOCBean.class, f);
		List<PojoBean> pojos = ioc.getPojos();
		if (pojos == null || pojos.isEmpty()) return;
		for (PojoBean pojo : pojos){
			pojoMap.put(pojo.getId(), pojo);
		}
	}
	
	@Override
	public PojoBean get(String key, Object... args) {
		return this.pojoMap.get(key);
	}
	
	public Map<String, PojoBean> getMap() {
		return this.pojoMap;
	}
	
}
