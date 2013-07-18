package com.eweb4j.core.config;

public interface Config {

	public void put(String key, Object value);
	
	public Object get(String key);
	
	public Integer getInt(String key, int defaultVal);
	
	public String getString(String key, String defaultVal);
	
	public Long getLong(String key, long defaultVal);
	
	public Float getFloat(String key, float defaultVal);
	
	public Double getDouble(String key, double defaultVal);
	
	public Boolean getBoolean(String key, boolean defaultVal);
	
    public Integer getInt(String key);
	
	public String getString(String key);
	
	public Long getLong(String key);
	
	public Float getFloat(String key);
	
	public Double getDouble(String key);
	
	public Boolean getBoolean(String key);
	
}
