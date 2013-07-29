package com.eweb4j.core.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map仓库实现
 * @author vivi
 *
 */
public class MapStorage<K,V> implements Storage<K, V>{

	/**
	 * 保存键值对的map
	 */
	private Map<K, V> values = null;
	
	public MapStorage(int size){
		this.values = new HashMap<K, V>(size);
	}
	
	public MapStorage(){
		this.values = new HashMap<K, V>();
	}
	
	public void put(K key, V value) {
		values.put(key, value);
	}

	public V get(K key) {
		return values.get(key);
	}

	public Integer getInt(K key, int defaultVal) {
		try {
			return Integer.parseInt(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public String getString(K key, String defaultVal) {
		try {
			return String.valueOf(get(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Long getLong(K key, long defaultVal) {
		try {
			return Long.parseLong(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Float getFloat(K key, float defaultVal) {
		try {
			return Float.parseFloat(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Double getDouble(K key, double defaultVal) {
		try {
			return Double.parseDouble(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Boolean getBoolean(K key, boolean defaultVal) {
		try {
			return Boolean.parseBoolean(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Integer getInt(K key) {
		try {
			return Integer.parseInt(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public String getString(K key) {
		try {
			return String.valueOf(get(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Long getLong(K key) {
		try {
			return Long.parseLong(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Float getFloat(K key) {
		try {
			return Float.parseFloat(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Double getDouble(K key) {
		try {
			return Double.parseDouble(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Boolean getBoolean(K key) {
		try {
			return Boolean.parseBoolean(String.valueOf(get(key)));
		} catch (Throwable e){
			
		}
		return null;
	}

	public boolean containsKey(K key) {
		return this.values.containsKey(key);
	}

	public V remove(K key) {
		return this.values.remove(key);
	}

	public boolean hasNext() {
		return !this.values.isEmpty();
	}

	public V next() {
		return this.values.entrySet().iterator().next().getValue();
	}

	public void clear() {
		this.values.clear();
	}

	public List<String> getListString(K key, String split) {
		String[] arr = this.getString(key, "").split(split);
		List<String> list = new ArrayList<String>(arr.length);
		for (String s : arr){
			list.add(s);
		}
		
		return list;
	}

	public String[] getArrayString(K key, String split) {
		return this.getString(key, "").split(split);
	}
}
