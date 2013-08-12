package com.eweb4j.core.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.configuration.xml.IOC;
import com.eweb4j.core.configuration.xml.Pojo;

/**
 * IOC Configuration
 * @author vivi
 *
 * @param <T>
 */
public abstract class PojoConfiguration<T> extends XMLConfiguration<T>{

	//IOC Pojo xml config cache
	protected Map<String, Pojo> pojoMap = null;

	@Override
	public void init() {
		try {
			//使用spring的simple-xml组件解析XML为POJO
			Serializer serializer = null;
			if (context == null)
				serializer = new Persister();
			else
				serializer = new Persister(context);
			
			File f = new File(xml);
			if (!f.exists()) throw new Exception("xml->"+f.getAbsolutePath()+" not found");
        	if (!f.isFile()) throw new Exception("xml->"+f.getAbsolutePath()+" is not a file");
        	IOC ioc = serializer.read(IOC.class, f);
			if (ioc == null) return ;
			
			List<Pojo> pojos = ioc.getPojos();
			if (pojos == null || pojos.isEmpty()) return;
			this.pojoMap = new HashMap<String, Pojo>(pojos.size());
			for (Pojo pojo : pojos){
				pojoMap.put(pojo.getId(), pojo);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
}
