package com.eweb4j.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Properties Util
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午05:54:00
 */
public class PropertiesUtil {

	private Map<String, String> map = new HashMap<String, String>();
	
	public final Set<String> keySet(){
		return map.keySet();
	}
	
	public final Number getNumber(String name){
		String val = map.get(name);
		if (!CommonUtil.isBlank(val)) 
			return CommonUtil.toDouble(val);
		
		return null;
	}
	
	public final Boolean getBoolean(String name){
		String val = map.get(name);
		if (!CommonUtil.isBlank(val)) 
			return CommonUtil.toBoolean(val);
		
		return null;
	}
	
	public final String getStr(String name){
		return map.get(name);
	}
	
	public final PropertiesUtil loadFile(String filePath) {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			Properties properties = new Properties();
			properties.load(in);
			
			//第一遍全部加进来
			Enumeration<?> en = properties.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = properties.getProperty(key);
				if (property == null)
					continue;
				
				if (!property.matches(RegexList.has_chinese_regexp)) {
					property = new String(property.getBytes("ISO-8859-1"), "UTF-8");
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
		
		return this;
	}
	
}
