package com.eweb4j.core.configurator;

import java.util.List;

/**
 * 仓库
 * @author vivi
 *
 */
public interface Storage<K, V> {

	public void put(K key, V value);
	
	public boolean containsKey(K key);
	
	public V remove(K key);
	
	public void clear();
	
	public boolean hasNext();
	
	public V next();
	
	public V get(K key);
	
	public List<String> getListString(K key, String split) ;
	
	public String[] getArrayString(K key, String split) ;
	
	public Integer getInt(K key, int defaultVal);
	
	public String getString(K key, String defaultVal);
	
	public Long getLong(K key, long defaultVal);
	
	public Float getFloat(K key, float defaultVal);
	
	public Double getDouble(K key, double defaultVal);
	
	public Boolean getBoolean(K key, boolean defaultVal);
	
    public Integer getInt(K key);
	
	public String getString(K key);
	
	public Long getLong(K key);
	
	public Float getFloat(K key);
	
	public Double getDouble(K key);
	
	public Boolean getBoolean(K key);
	
}
