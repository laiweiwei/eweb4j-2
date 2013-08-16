package com.eweb4j.core.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractConfiguration<K, V> implements Configuration<K, V>{
	
	public void init(){}
	
	public abstract V get(K key, Object... args) ;

	public String getString(K key, String defaultVal) {
		try {
			V v = get(key);
			if (v == null) return defaultVal;
			return String.valueOf(v);
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Integer getInteger(K key, Integer defaultVal) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public int getInt(K key, int defaultVal) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Long getLong(K key, Long defaultVal) {
		try {
			return Long.parseLong(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public long getLong(K key, long defaultVal) {
		try {
			return Long.parseLong(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public float getFloat(K key, float defaultVal) {
		try {
			return Float.parseFloat(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Float getFloat(K key, Float defaultVal) {
		try {
			return Float.parseFloat(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public double getDouble(K key, double defaultVal) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public Double getDouble(K key, Double defaultVal) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Boolean getBoolean(K key, Boolean defaultVal) {
		try {
			return Boolean.parseBoolean(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}
	
	public boolean getBoolean(K key, boolean defaultVal) {
		try {
			return Boolean.parseBoolean(getString(key));
		} catch (Throwable e){
			
		}
		return defaultVal;
	}

	public Integer getInteger(K key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Throwable e){
			
		}
		return null;
	}
	
	public int getInt(K key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Throwable e){
			
		}
		
		return 0;
	}


	public String getString(K key) {
		try {
			return getString(key, null);
		} catch (Throwable e){
			
		}
		return null;
	}

	public Long getLong(K key) {
		try {
			return Long.parseLong(getString(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Float getFloat(K key) {
		try {
			return Float.parseFloat(getString(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Double getDouble(K key) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public Boolean getBoolean(K key) {
		try {
			return Boolean.parseBoolean(getString(key));
		} catch (Throwable e){
			
		}
		return null;
	}

	public List<String> getListString(K key, String split) {
		String[] arr = this.getString(key, "").split(split);
		List<String> list = new ArrayList<String>(arr.length);
		for (String s : arr){
			list.add(s);
		}
		
		return list;
	}

	public List<Double> getListDouble(K key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Double> list = new ArrayList<Double>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Double.parseDouble(s));
		}
		
		return list;
	}

	public List<Float> getListFloat(K key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Float> list = new ArrayList<Float>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Float.parseFloat(s));
		}
		
		return list;
	}

	public List<Long> getListLong(K key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Long> list = new ArrayList<Long>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Long.parseLong(s));
		}
		
		return list;
	}

	public List<Integer> getListInteger(K key, String split) {
		List<String> _list = getListString(key, split);
		if (_list == null || _list.isEmpty()) return null;
		List<Integer> list = new ArrayList<Integer>(_list.size());
		for (String s : _list) {
			if (s == null || s.trim().length() == 0) continue;
			
			list.add(Integer.parseInt(s));
		}
		
		return list;
	}

	public List<Double> getListDouble(K key) {
		return getListDouble(key, ",");
	}

	public List<Float> getListFloat(K key) {
		return getListFloat(key, ",");
	}

	public List<Long> getListLong(K key) {
		return getListLong(key, ",");
	}

	public List<Integer> getListInteger(K key) {
		return getListInteger(key, ",");
	}

	public List<String> getListString(K key) {
		return getListString(key, ",");
	}

	public int size() {
		return this.getMap().size();
	}

	public boolean isEmpty() {
		return getMap().isEmpty();
	}

	public boolean containsKey(Object key) {
		return getMap().containsKey(key);
	}

	public boolean containsValue(Object value) {
		return getMap().containsValue(value);
	}

	public V get(Object key) {
		return getMap().get(key);
	}

	public V put(K key, V value) {
		return getMap().put(key, value);
	}

	public V remove(Object key) {
		return getMap().remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		getMap().putAll(m);
	}

	public void clear() {
		getMap().clear();
	}

	public Set<K> keySet() {
		return getMap().keySet();
	}

	public Collection<V> values() {
		return getMap().values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return getMap().entrySet();
	}
}
