package com.eweb4j.core.configuration;

import java.util.List;

import javax.sql.DataSource;

import com.eweb4j.core.orm.EntityRelationMapping;

/**
 * 配置工厂
 * @author vivi
 *
 */
public interface ConfigurationFactory {

	/**
	 * 设置配置容器，若已存在相同ID的将会替换
	 * @param id 容器ID
	 * @return
	 */
	public void setConfiguration(String id, Configuration<String, ?> configuration);
	
	/**
	 * 添加配置容器，若已存在相同ID的将会添加失败
	 * @param id 容器ID
	 * @return
	 */
	public void addConfiguration(String id, Configuration<String, ?> configuration);
	
	/**
	 * 根据ID获取对应的配置容器
	 * @param id 容器ID
	 * @return 容器实例
	 */
	public <V> Configuration<String, V> getConfiguration(String id);
	
	///////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取base配置信息容器
	 * @return
	 */
	public <V> Configuration<String, V> getBaseConfig();
	
	/**
	 * 设置base配置信息容器
	 * @param <V>
	 */
	public <V> void setBaseConfig(Configuration<String, V> baseConfig);
	
	/**
	 * 快速获取根目录的绝对路径
	 * @return
	 */
	public String getAbsolutePathOfRoot();
	
	/**
	 * 快速获取视图相对于根目录的相对路径
	 * @return
	 */
	public String getRelativePathOfView();
	
	/**
	 * 快速获取视图目录绝对路径
	 * @return
	 */
	public String getAbsolutePathOfView();
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取IOC配置信息容器
	 * @param <V>
	 * @return
	 */
	public <V> Configuration<String, V> getIocConfig();
	
	/**
	 * 设置IOC配置信息容器
	 * @param <V>
	 * @param iocConfig
	 */
	public <V> void setIocConfig(Configuration<String, V> iocConfig);
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取JDBC配置信息容器
	 * @param <V>
	 * @return
	 */
	public <V> Configuration<String, V> getJdbcConfig();
	
	/**
	 * 设置JDBC配置信息容器
	 * @param <V>
	 * @param jdbcConfig
	 */
	public <V> void setJdbcConfig(Configuration<String, V> jdbcConfig);
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取插件配置信息容器
	 * @param <V>
	 * @return
	 */
	public <V> Configuration<String, V> getPluginConfig();
	
	/**
	 * 设置插件配置信息容器
	 * @param <V>
	 * @param pluginConfig
	 */
	public <V> void setPluginConfig(Configuration<String, V> pluginConfig);
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取数据源容器
	 * @return
	 */
	public Configuration<String, DataSource> getDataSourceConfig();
	
	/**
	 * 设置数据源容器
	 * @param dataSourceStorage
	 */
	public void setDataSourceConfig(Configuration<String, DataSource> dataSourceStorage);
	
	/**
	 * 设置数据源，若存在相同名字的将会替换
	 * @param dsName
	 * @param ds
	 */
	public void setDataSource(String dsName, DataSource ds);
	
	/**
	 * 添加数据源，若存在相同名字的将添加失败
	 * @param dsName
	 * @param ds
	 */
	public void addDataSource(String dsName, DataSource ds);
	
	/**
	 * 快速获取默认的数据源
	 * @return
	 */
	public DataSource getDefaultDataSource();
	
	/**
	 * 快速获取指定名称的数据源
	 * @param dsName
	 * @return
	 */
	public DataSource getDataSource(String dsName);
	
	/**
	 * 快速获取默认的数据源名
	 * @return
	 */
	public String getDefaultDataSourceName();
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 快速获取监听器实现类列表
	 * @return
	 */
	public List<String> getListeners();
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取实体类映射信息容器
	 * @return
	 */
	public Configuration<String, EntityRelationMapping> getEntityRelationMappingConfig();
	
	/**
	 * 设置实体类映射信息，会覆盖已有的
	 */
	public void setEntityRelationMapping(String entityClass, EntityRelationMapping erm);
	
	/**
	 * 添加实体类映射信息，不会覆盖已有的
	 */
	public void addEntityRelationMapping(String entityClass, EntityRelationMapping erm);
	
	/**
	 * 快速获取实体类映射信息
	 * @param entityClass 实体类
	 * @return
	 */
	public EntityRelationMapping getEntityRelationMapping(Class<?> entityClass);
	
	/**
	 * 快速获取实体类映射信息
	 * @param <T>
	 * @param entityClass 实体类名
	 * @return
	 */
	public EntityRelationMapping getEntityRelationMapping(String entityClass);
	
	///////////////////////////////////////////////////////////////////////////////////////
	
}
