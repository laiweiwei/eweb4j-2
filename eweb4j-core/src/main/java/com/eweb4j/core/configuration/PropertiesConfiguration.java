package com.eweb4j.core.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件读取器
 * <li>修改properties配置文件后不用重启即可获取最新的值</li>
 * @author vivi
 *
 */
public class PropertiesConfiguration implements Configuration<String, String>{

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
	
	public void put(String key, String value) {
		this.values.put(key, value);
	}

	public String get(String key) {
		try {
			return _get(key, null);
		} catch (Throwable e) {
		}
		return null;
	}

	public String getString(String key, String defaultVal) {
		try {
			return get(key);
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Integer getInteger(String key, Integer defaultVal) {
		try {
			return Integer.parseInt(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public int getInt(String key, int defaultVal) {
		try {
			return Integer.parseInt(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Long getLong(String key, Long defaultVal) {
		try {
			return Long.parseLong(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public long getLong(String key, long defaultVal) {
		try {
			return Long.parseLong(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public float getFloat(String key, float defaultVal) {
		try {
			return Float.parseFloat(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Float getFloat(String key, Float defaultVal) {
		try {
			return Float.parseFloat(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public double getDouble(String key, double defaultVal) {
		try {
			return Double.parseDouble(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Double getDouble(String key, Double defaultVal) {
		try {
			return Double.parseDouble(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Boolean getBoolean(String key, Boolean defaultVal) {
		try {
			return Boolean.parseBoolean(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public boolean getBoolean(String key, boolean defaultVal) {
		try {
			return Boolean.parseBoolean(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Integer getInteger(String key) {
		try {
			return Integer.parseInt(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}
	
	public int getInt(String key) {
		try {
			return Integer.parseInt(get(key));
		} catch (Throwable e){
			
		}
		
		return 0;
	}


	public String getString(String key) {
		try {
			return get(key);
		} catch (Throwable e){
			
		}
		return null;
	}

	public Long getLong(String key) {
		try {
			return Long.parseLong(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Float getFloat(String key) {
		try {
			return Float.parseFloat(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Double getDouble(String key) {
		try {
			return Double.parseDouble(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Boolean getBoolean(String key) {
		try {
			return Boolean.parseBoolean(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public boolean containsKey(String key) {
		return this.values.containsKey(key);
	}

	public String remove(String key) {
		return values.remove(key);
	}

	public boolean hasNext() {
		return !values.isEmpty();
	}

	public String next() {
		return values.entrySet().iterator().next().getValue();
	}

	public void clear() {
		this.values.clear();
	}

	public List<String> getListString(String key, String split) {
		String[] arr = this.getString(key, "").split(split);
		List<String> list = new ArrayList<String>(arr.length);
		for (String s : arr){
			list.add(s);
		}
		
		return list;
	}

	public List<Double> getListDouble(String key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Double> list = new ArrayList<Double>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Double.parseDouble(s));
		}
		
		return list;
	}

	public List<Float> getListFloat(String key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Float> list = new ArrayList<Float>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Float.parseFloat(s));
		}
		
		return list;
	}

	public List<Long> getListLong(String key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Long> list = new ArrayList<Long>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Long.parseLong(s));
		}
		
		return list;
	}

	public List<Integer> getListInteger(String key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Integer> list = new ArrayList<Integer>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Integer.parseInt(s));
		}
		
		return list;
	}

	public Map<String, String> getMap() {
		return this.values;
	}

	public List<Double> getListDouble(String key) {
		return getListDouble(key, ",");
	}

	public List<Float> getListFloat(String key) {
		return getListFloat(key, ",");
	}

	public List<Long> getListLong(String key) {
		return getListLong(key, ",");
	}

	public List<Integer> getListInteger(String key) {
		return getListInteger(key, ",");
	}

	public List<String> getListString(String key) {
		return getListString(key, ",");
	}
	
	public static void main(String[] args) throws Throwable{
		String filePath = "resources/db.properties";
		Configuration<String, String> config = new PropertiesConfiguration(filePath);
		System.out.println(config.getMap());
	}
}
