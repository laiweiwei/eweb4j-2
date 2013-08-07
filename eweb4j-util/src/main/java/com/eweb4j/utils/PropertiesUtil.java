package com.eweb4j.utils;

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
 * Properties Util
 * <p></p>
 * @author weiwei l.weiwei@163.com
 */
public class PropertiesUtil {

	private final static Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
	private final static Map<String, Long> fileLastModified = new HashMap<String, Long>();
	
	public final static String getString(String fileName, String key){
		return getString(fileName, key, null);
	}
	public final static String getString(String fileName, String key, String defaultValue){
		String path = PropertiesUtil.class.getResource("/").getFile();
		File file = new File(path + File.separator + fileName + ".properties");
		if (!file.exists())
			return defaultValue;
		Long lastMod = fileLastModified.get(fileName);
		Long fileLastMod = file.lastModified();
		if (lastMod == null || lastMod < fileLastMod){
			loadFile(fileName, file.getAbsolutePath());
			fileLastModified.put(fileName, fileLastMod);
		}
		
		Map<String, String> map = maps.get(fileName);
		if (map == null) return defaultValue;
		
		String val = map.get(key);
		return val == null ? defaultValue : val;
	}
	
	private final static void loadFile(String fileName, String abPath) {
		InputStream in = null;
		try {
			File file = new File(abPath);
			in = new BufferedInputStream(new FileInputStream(file));
			Properties properties = new Properties();
			properties.load(in);
			
			Enumeration<?> en = properties.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = properties.getProperty(key);
				if (property == null)
					continue;
				
				if (!property.matches(RegexList.has_chinese_regexp)) {
					property = new String(property.getBytes("ISO-8859-1"), "UTF-8");
				}
				
				Map<String, String> map = maps.get(fileName);
				if (map == null) {
					map = new HashMap<String, String>();
					maps.put(fileName, map);
				}
				
				map.put(key, property);
			}
			
		} catch (Throwable e){
			e.printStackTrace();
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
	
	public static void main(String[] args) throws InterruptedException {
		while (true) {
			System.out.println(PropertiesUtil.getString("qhee-api", "zbus.enabled"));
			Thread.sleep(5*1000);
		}
	}
}
