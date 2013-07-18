package com.eweb4j.core.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigImpl implements Config{

	private Map<String, Object> values = new HashMap<String, Object>();
	
	public void put(String key, Object value) {
		values.put(key, value);
	}

	public Object get(String key) {
		return values.get(key);
	}

	public Integer getInt(String key, int defaultVal) {
		try {
			return Integer.parseInt(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public String getString(String key, String defaultVal) {
		try {
			return String.valueOf(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Long getLong(String key, long defaultVal) {
		try {
			return Long.parseLong(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Float getFloat(String key, float defaultVal) {
		try {
			return Float.parseFloat(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Double getDouble(String key, double defaultVal) {
		try {
			return Double.parseDouble(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Boolean getBoolean(String key, boolean defaultVal) {
		try {
			return Boolean.parseBoolean(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Integer getInt(String key) {
		try {
			return Integer.parseInt(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public String getString(String key) {
		try {
			return String.valueOf(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Long getLong(String key) {
		try {
			return Long.parseLong(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Float getFloat(String key) {
		try {
			return Float.parseFloat(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Double getDouble(String key) {
		try {
			return Double.parseDouble(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Boolean getBoolean(String key) {
		try {
			return Boolean.parseBoolean(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}
}
