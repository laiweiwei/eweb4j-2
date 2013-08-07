package com.eweb4j.core.configuration;

import java.util.List;
import java.util.Map;

/**
 * 配置
 * @author vivi
 *
 */
public interface Configuration<K, V> {

	public Map<K, V> toMap();
	
	public void put(K key, V value);
	
	public boolean containsKey(K key);
	
	public V remove(K key);
	
	public void clear();
	
	public boolean hasNext();
	
	public V next();
	
	public V get(K key);
	
	public List<Double> getListDouble(K key, String split) ;
	
	public List<Float> getListFloat(K key, String split) ;
	
	public List<Long> getListLong(K key, String split) ;
	
	public List<Integer> getListInteger(K key, String split) ;
	
	public List<String> getListString(K key, String split) ;
	
	public String getString(K key, String defaultVal);
	
	public Integer getInteger(K key, Integer defaultVal);
	
	public int getInt(K key, int defaultVal);
	
	public Long getLong(K key, Long defaultVal);
	
	public long getLong(K key, long defaultVal);
	
	public Float getFloat(K key, Float defaultVal);
	
	public float getFloat(K key, float defaultVal);
	
	public Double getDouble(K key, Double defaultVal);
	
	public double getDouble(K key, double defaultVal);
	
	public Boolean getBoolean(K key, Boolean defaultVal);
	
	public boolean getBoolean(K key, boolean defaultVal);
	
    public Integer getInteger(K key);
    
    public int getInt(K key);
	
	public String getString(K key);
	
	public Long getLong(K key);
	
	public Float getFloat(K key);
	
	public Double getDouble(K key);
	
	public Boolean getBoolean(K key);
	
}
