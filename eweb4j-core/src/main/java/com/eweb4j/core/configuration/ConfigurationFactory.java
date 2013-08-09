package com.eweb4j.core.configuration;

import java.util.List;

import javax.sql.DataSource;


public interface ConfigurationFactory {

	/**
	 * 添加配置
	 * @param id 配置ID
	 * @param configuration
	 * @return
	 */
	public Configuration<String, ?> addConfiguration(String id, Configuration<String, ?> configuration);
	
	/**
	 * 获取base配置信息
	 * @return
	 */
	public <V> Configuration<String, V> getBaseConfig();
	
	/**
	 * 获取jdbc配置信息
	 * @param <V>
	 * @return
	 */
	public <V> Configuration<String, V> getJdbcConfig();
	
	/**
	 * 获取feature容器
	 * @param <V>
	 * @return
	 */
	public <V> Configuration<String, V> getFeature();
	
	/**
	 * 根据ID获取对应的配置内容
	 * @param id
	 * @return
	 */
	public <V> Configuration<String, V> getConfiguration(String id);
	
	/**
	 * 获取默认的数据源名
	 * @return
	 */
	String getDefaultDataSourceName();
	
	/**
	 * 获取默认的数据源
	 * @return
	 */
	DataSource getDefaultDataSource();
	
	/**
	 * 获取指定名称的数据源
	 * @param dsName
	 * @return
	 */
	DataSource getDataSource(String dsName);
	
	/**
	 * 获取根目录的绝对路径
	 * @return
	 */
	String getAbsolutePathOfRoot();
	
	/**
	 * 获取视图相对于根目录的相对路径
	 * @return
	 */
	String getRelativePathOfView();
	
	/**
	 * 获取视图目录绝对路径
	 * @return
	 */
	String getAbsolutePathOfView();
	
	/**
	 * 获取监听器实现类列表
	 * @return
	 */
	List<String> getListeners();
	
	/**
	 * 获取插件实现类列表
	 * @return
	 */
	List<String> getPlugins();
	
	/**
	 * 获取JPA注解信息
	 * @param <T>
	 * @param entityClass 实体类
	 * @return
	 */
	public <T> T getJPA(Class<?> entityClass);
	
	/**
	 * 获取JPA注解信息
	 * @param <T>
	 * @param entityClass 实体类名
	 * @return
	 */
	public <T> T getJPA(String entityClass);
	
}
