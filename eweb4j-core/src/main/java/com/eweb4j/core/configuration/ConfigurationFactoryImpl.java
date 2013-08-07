package com.eweb4j.core.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.configuration.xml.EWeb4j;
import com.eweb4j.core.configuration.xml.FilePath;
import com.eweb4j.core.configuration.xml.Property;

public class ConfigurationFactoryImpl implements ConfigurationFactory{

	private Map<String, Configuration<String, ?>> configs = null;
	
	public ConfigurationFactoryImpl(Configuration<String, String> contextConfig, String xml) {
		this.configs = new HashMap<String, Configuration<String, ?>>();
		
		//加载eweb4j.properties文件
		try {
			Map<String, String> context = new HashMap<String, String>(1);
			if (contextConfig != null){
				//若存在该文件，则将配置内容加载内存中
				this.configs.put("eweb4j", contextConfig);
				//把配置内容当做是xml文件解析的上下文环境
				context = contextConfig.toMap();
			}else{
				this.configs.put("eweb4j", new MapConfiguration<String,String>());
			}
       
        	//解析xml文件
        	File f = new File(xml);
        	if (f.exists() && f.isFile()) {
        		Serializer serializer = new Persister(context);
        		EWeb4j eweb4j = serializer.read(EWeb4j.class, f);
        		if (eweb4j != null){
        			List<com.eweb4j.core.configuration.xml.Configuration> configs = eweb4j.getConfigurations();
        			for (com.eweb4j.core.configuration.xml.Configuration config : configs) {
        				Configuration<String, String> configuration = null;
        				String id = config.getId();
        				String type = config.getType();
        				FilePath filePathBean = config.getFile();
        				if ("properties".equals(type)) {
    						String filePath = filePathBean.getPath();
    						File propFile = new File(filePath);
    						if (!propFile.exists() || !propFile.isFile()) continue;
    						configuration = new PropertiesConfiguration(filePath);
    						
        				}else {
        					List<Property> propertiesBean = config.getProperties();
        					if (propertiesBean == null || propertiesBean.isEmpty()) continue;
        					configuration = new MapConfiguration<String, String>(propertiesBean.size());
        					for (Property p : propertiesBean) {
        						configuration.put(p.getName(), p.getValue());
        					}
        				}
        				
        				//将对应ID的配置信息添加到内存里
        				this.configs.put(id, configuration);
        			}
        		}
        	}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取默认的配置内容
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getConfiguration(){
		return (Configuration<String, V>) configs.get("eweb4j");
	}
	
	/**
	 * 根据ID获取对应的配置内容
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getConfiguration(String id){
		return (Configuration<String, V>) configs.get(id);
	}
	
	public static void main(String[] args) throws Exception{
		
	}
}
