package com.eweb4j.core.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件读取器
 * <li>修改properties配置文件后不用重启即可获取最新的值</li>
 * @author vivi
 *
 */
public class PropertiesConfiguration extends AbstractConfiguration<String, String>{

	private String filePath = null;
	private final Map<String, String> values = new HashMap<String, String>();
	private final Map<String, Long> fileLastModified = new HashMap<String, Long>();
	
	public PropertiesConfiguration(String filePath) throws Throwable{
		this.filePath = filePath;
		this._load_file();
	}
	
	private final void _load_file() throws Throwable {
		InputStream in = null;
		try {
			File file = new File(filePath);
			in = new BufferedInputStream(new FileInputStream(file));
			Properties properties = new Properties();
			properties.load(in);
			
			Enumeration<?> en = properties.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = properties.getProperty(key);
				if (property == null)
					continue;
				
				if (!property.matches("[\u4e00-\u9fa5]+")) {
					property = new String(property.getBytes("ISO-8859-1"), "UTF-8");
				}
				
				this.values.put(key, property);
			}
			
		} catch (Throwable e){
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private final String _get(String key, String defaultValue) throws Throwable {
		File file = new File(filePath);
		if (!file.exists())
			return defaultValue;
		
		Long lastMod = this.fileLastModified.get(filePath);
		Long fileLastMod = file.lastModified();
		if (lastMod == null || lastMod < fileLastMod){
			this._load_file();
			this.fileLastModified.put(filePath, fileLastMod);
		}
		
		String val = this.values.get(key);
		return val == null ? defaultValue : val;
	}
	
	public String get(String key) {
		try {
			return _get(key, null);
		} catch (Throwable e) {
		}
		return null;
	}
	
	public Map<String, String> getMap() {
		return this.values;
	}
	
	public void put(String key, String value) {
		this.values.put(key, value);
	}
	
	public String get(String key, Object... args) {
		return getMap().get(key);
	}
	
	public static void main(String[] args) throws Throwable{
		String filePath = "resources/db.properties";
		Configuration<String, String> config = new PropertiesConfiguration(filePath);
		System.out.println(config.getMap());
	}
	
}
