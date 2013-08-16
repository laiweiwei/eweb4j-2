package com.eweb4j.core.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Map 存储结构的配置容器
 * @author vivi
 *
 */
public class MapConfiguration<K, V> extends AbstractConfiguration<K, V>{

	/**
	 * 保存键值对的map
	 */
	private Map<K, V> values = null;
	
	public MapConfiguration(){
		this.values = new HashMap<K, V>();
	}
	
	public MapConfiguration(int size){
		this.values = new HashMap<K, V>(size);
	}

	public Map<K, V> getMap() {
		return this.values;
	}
	
	public V get(K key, Object... args) {
		return getMap().get(key);
	}
	
}
