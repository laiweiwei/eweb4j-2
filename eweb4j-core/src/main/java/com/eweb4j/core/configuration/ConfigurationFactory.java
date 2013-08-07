package com.eweb4j.core.configuration;


public interface ConfigurationFactory {

	/**
	 * 获取默认的配置内容
	 * @return
	 */
	public <V> Configuration<String, V> getConfiguration();
	
	/**
	 * 根据ID获取对应的配置内容
	 * @param id
	 * @return
	 */
	public <V> Configuration<String, V> getConfiguration(String id);
	
}
