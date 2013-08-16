package com.eweb4j.core.configuration;

import static com.eweb4j.core.EWeb4J.Constants.Configurations.BASE_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.DATA_SOURCE_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.IOC_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.JDBC_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.ENTITY_RELATION_MAPPING_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.LISTENER_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.PLUGIN_ID;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.EWeb4J.Constants.Configurations.Types;
import com.eweb4j.core.configuration.xml.ConfigurationBean;
import com.eweb4j.core.configuration.xml.EWeb4JBean;
import com.eweb4j.core.configuration.xml.FileBean;
import com.eweb4j.core.configuration.xml.PropertyBean;
import com.eweb4j.core.orm.EntityRelationMapping;

public class ConfigurationFactoryImpl implements ConfigurationFactory{

	private Map<String, Configuration<String, ?>> configs = null;
	
	/**
	 * 读取base配置信息
	 * @param xml
	 * @return
	 * @throws Throwable
	 */
	private Configuration<String, String> readBaseConfigFromXml(File xml) throws Throwable{
		if (xml == null) return null;
		
		//使用spring的simple-xml组件解析XML为POJO
		Serializer serializer = new Persister();
		EWeb4JBean eweb4j = serializer.read(EWeb4JBean.class, xml);
		if (eweb4j == null) return null;
		
		List<ConfigurationBean> configs = eweb4j.getConfigurations();
		for (ConfigurationBean config : configs) {
			Configuration<String, String> configuration = null;
			String id = config.getId();
			//只要base的配置
			if (!BASE_ID.equals(id)) continue;
			List<PropertyBean> propertiesBean = config.getProperties();
			if (propertiesBean == null || propertiesBean.isEmpty()) continue;
			configuration = new MapConfiguration<String, String>(propertiesBean.size());
			for (PropertyBean p : propertiesBean) {
				//若不开启，跳过
				if (0 == p.getEnabled()) continue;
				
				configuration.put(p.getName(), p.getValue());
			}
			
			return configuration;
		}
		
		return null;
	}
	
	private void storeProperties(Configuration<String, Object> configuration, List<PropertyBean> propertiesBean){
		Map<String, StringBuilder> arrayMap = new HashMap<String, StringBuilder>();
		for (PropertyBean p : propertiesBean) {
			//若不开启，跳过
			if (0 == p.getEnabled()) continue;
			
			if (!arrayMap.containsKey(p.getName()))
				arrayMap.put(p.getName(), new StringBuilder());
			StringBuilder sb = arrayMap.get(p.getName());
			if (!configuration.containsKey(p.getName())) {
				configuration.put(p.getName(), p.getValue());
			}
			
			Object v = configuration.get(p.getName());
			if (v != null) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(p.getValue());
				arrayMap.put(p.getName(), sb);
			}
			configuration.put(p.getName(), sb.toString());
		}
	}
	
	/**
	 * 读取XML文件里的配置信息并存储到内存中
	 * @param xml 配置文件
	 * @param context 上下文，用来替换${}的变量引用
	 * @throws Throwable
	 */
	private void storeConfigsFromXml(File xml, Map<?,?> context) throws Throwable{
		//使用spring的simple-xml组件解析XML为POJO
		Serializer serializer = new Persister(context);
		EWeb4JBean eweb4j = serializer.read(EWeb4JBean.class, xml);
		if (eweb4j == null) return ;
	
		List<ConfigurationBean> configs = eweb4j.getConfigurations();
		for (ConfigurationBean config : configs) {
			Configuration<String, Object> configuration = null;
			String id = config.getId();
			String type = config.getType();
			List<PropertyBean> propertyBeans = config.getProperties();
			//根据不同的类型处理不同配置信息的获取来源
			if (Types.PROPERTIES.equals(type)) {
				List<FileBean> filePaths = config.getFiles();
				configuration = new MapConfiguration<String, Object>();
				if (filePaths != null && !filePaths.isEmpty()) {
					for (FileBean filePathBean : filePaths) {
						//若不开启，跳过
						if (0 == filePathBean.getEnabled()) continue;
						String _path = filePathBean.getPath();
						if (_path == null || _path.trim().length() == 0) continue;
						final String filePath = EWeb4J.Constants.resolve_path(_path);
						File f = new File(filePath);
						if (!f.exists()) throw new Exception("file->"+f.getAbsolutePath()+" not found");
			        	if (!f.isFile()) throw new Exception("file->"+f.getAbsolutePath()+" is not a file");
						//读取properties文件
			        	configuration.putAll(new PropertiesConfiguration(filePath).getMap());
					}
				}
			} else if (Types.XML.equals(type)){
				List<FileBean> filePaths = config.getFiles();
				if (filePaths != null && !filePaths.isEmpty()) {
					String configClass = config.getHolder();
					@SuppressWarnings("unchecked")
					Class<XMLConfiguration<String, Object>> cls = (Class<XMLConfiguration<String, Object>>) Thread.currentThread().getContextClassLoader().loadClass(configClass);
					configuration = cls.newInstance();
					XMLConfiguration<String, Object> c = (XMLConfiguration<String, Object>)configuration;
					for (FileBean filePathBean : filePaths) {
						//若不开启，跳过
						if (0 == filePathBean.getEnabled()) continue;
						String _path = filePathBean.getPath();
						if (_path == null || _path.trim().length() == 0) continue;
						final String filePath = EWeb4J.Constants.resolve_path(_path);
						File f = new File(filePath);
						if (!f.exists()) throw new Exception("file->"+f.getAbsolutePath()+" not found");
			        	if (!f.isFile()) throw new Exception("file->"+f.getAbsolutePath()+" is not a file");
						
			        	//解析xml文件
			        	c.parseXml(f.getAbsolutePath(), context);
					}
					
					//存储<property>节点
					this.storeProperties(configuration, propertyBeans);
					
					//将对应ID的配置信息添加到内存里
					this.configs.put(id, configuration);
				}
				
				continue;
			} else {
				configuration = new MapConfiguration<String, Object>(propertyBeans.size());
			}
			
			//存储<property>节点
			this.storeProperties(configuration, propertyBeans);
			
			//将对应ID的配置信息添加到内存里
			this.configs.put(id, configuration);
		}
	}
	
	/**
	 * 初始化
	 * @param xml
	 */
	private void init(final String _xml){
		String xml = _xml;
		if (xml == null || xml.trim().length() == 0) 
			xml = EWeb4J.Constants.config_xml;
		else 
			xml = EWeb4J.Constants.resolve_path(xml);
		
		this.configs = new HashMap<String, Configuration<String, ?>>();
		try {
			Map<String, String> context = null;
        	File f = new File(xml);
        	if (!f.exists()) throw new Exception("xml->"+f.getAbsolutePath()+" not found");
        	if (!f.isFile()) throw new Exception("xml->"+f.getAbsolutePath()+" is not a file");
        	//首先获取base的配置信息
        	Configuration<String, String> baseConfig = this.readBaseConfigFromXml(f);
        	if (baseConfig != null){
        		//转成上下文
        		context = baseConfig;
        	}else{
        		context = new HashMap<String, String>(1);
        	}
        	
        	this.storeConfigsFromXml(f, context);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConfigurationFactoryImpl(){
		init(null);
	}
	
	/**
	 * 构造方法
	 * @param xml
	 */
	public ConfigurationFactoryImpl(String xml) {
		init(xml);
	}
	
	/**
	 * 获取数组字符串内容
	 * @param id
	 * @param key
	 * @return
	 */
	private List<String> getListString(String id, String key){
		return getListString(id, key, ",");
	}
	
	/**
	 * 获取数组字符串内容
	 * @param id
	 * @param key
	 * @param split
	 * @return
	 */
	private List<String> getListString(String id, String key, String split){
		Configuration<String, ?> config = this.configs.get(id);
		if (config == null) return null;
		
		return config.getListString(key, split);
	}
	
	/**
	 * 获取字符串内容
	 * @param id
	 * @param key
	 * @return
	 */
	private String getString(String id, String key){
		return getString(id, key, null);
	}
	
	/**
	 * 获取字符串内容
	 * @param id
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	private String getString(String id, String key, String defaultVal){
		Configuration<String, ?> config = this.configs.get(id);
		if (config == null) return null;
		return config.getString(key, defaultVal);
	}
	
	/**
	 * 设置配置容器
	 */
	public void setConfiguration(String id, Configuration<String, ?> configuration) {
		this.configs.put(id, configuration);
	}

	/**
	 * 添加配置容器
	 */
	public void addConfiguration(String id, Configuration<String, ?> configuration) {
		if (!this.configs.containsKey(id))
			this.configs.put(id, configuration);
	}

	
	/**
	 * 获取base配置信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getBaseConfig(){
		return (Configuration<String, V>) this.configs.get(BASE_ID);
	}
	
	/**
	 * 设置基本配置
	 */
	public <V> void setBaseConfig(Configuration<String, V> baseConfig) {
		this.configs.put(BASE_ID, baseConfig);
	}

	/**
	 * 获取JDBC配置容器
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getJdbcConfig(){
		return (Configuration<String, V>) configs.get(JDBC_ID);
	}
	
	/**
	 * 设置JDBC配置容器
	 */
	public <V> void setJdbcConfig(Configuration<String, V> jdbcConfig) {
		this.configs.put(JDBC_ID, jdbcConfig);
	}
	
	/**
	 * 获取插件配置信息容器
	 * @param <V>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getPluginConfig(){
		return (Configuration<String, V>) configs.get(PLUGIN_ID);
	}
	
	/**
	 * 设置插件配置信息容器
	 * @param <V>
	 * @param pluginConfig
	 */
	public <V> void setPluginConfig(Configuration<String, V> pluginConfig){
		this.configs.put(PLUGIN_ID, pluginConfig);
	}
	
	/**
	 * 设置IOC配置容器
	 */
	public <V> void setIocConfig(Configuration<String, V> iocConfig) {
		this.configs.put(IOC_ID, iocConfig);
	}
	
	/**
	 * 获取IOC信息容器
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getIocConfig() {
		return (Configuration<String, V>)configs.get(IOC_ID);
	}
	
	/**
	 * 根据ID获取对应的配置内容
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getConfiguration(String id){
		return (Configuration<String, V>) configs.get(id);
	}
	
	/**
	 * 获取默认数据源名
	 * @return
	 */
	public String getDefaultDataSourceName(){
		List<String> list = this.getListString(JDBC_ID, "ds.names");
		return list == null ? "ds" : list.isEmpty() ? "ds" : list.get(0);
	}
	
	/**
	 * 获取默认的数据源实例
	 */
	public DataSource getDefaultDataSource(){
		String dsName = getDefaultDataSourceName();
		Configuration<String, ?> config = this.configs.get(DATA_SOURCE_ID);
		if (config == null) return null;
		return (DataSource) config.get(dsName);
	}
	
	/**
	 * 获取指定名字的数据源实例
	 */
	public DataSource getDataSource(String dsName){
		Configuration<String, DataSource> config = getDataSourceConfig();
		if (config == null) return null;
		return (DataSource) config.get(dsName);
	}
	
	/**
	 * 获取数据源容器
	 */
	@SuppressWarnings("unchecked")
	public Configuration<String, DataSource> getDataSourceConfig() {
		return (Configuration<String, DataSource>) this.configs.get(DATA_SOURCE_ID);
	}

	/**
	 * 设置数据源容器
	 */
	public void setDataSourceConfig(Configuration<String, DataSource> dataSourceConfig) {
		if (dataSourceConfig != null)
			this.configs.put(DATA_SOURCE_ID, dataSourceConfig);
	}

	/**
	 * 设置数据源
	 */
	public void setDataSource(String dsName, DataSource ds) {
		Configuration<String, DataSource> config = getDataSourceConfig();
		if (config != null)
			config.put(dsName, ds);
	}

	/**
	 * 添加数据源
	 */
	public void addDataSource(String dsName, DataSource ds) {
		Configuration<String, DataSource> config = getDataSourceConfig();
		if (config != null && !config.containsKey(dsName))
			config.put(dsName, ds);
	}
	
	/**
	 * 获取根目录的绝对路径
	 * @return
	 */
	public String getAbsolutePathOfRoot(){
		return this.getString(BASE_ID, "absolute_path_of_root");
	}
	
	/**
	 * 获取视图相对于根目录的相对路径
	 * @return
	 */
	public String getRelativePathOfView(){
		return this.getString(BASE_ID, "relative_path_of_view", "/views/");
	}
	
	/**
	 * 获取视图目录的绝对路径
	 * @return
	 */
	public String getAbsolutePathOfView(){
		return getAbsolutePathOfRoot() + getRelativePathOfView();
	}

	/**
	 * 获取监听器实现类列表
	 */
	public List<String> getListeners() {
		return this.getListString(LISTENER_ID, "class");
	}
	
	/**
	 * 获取实体类映射信息容器
	 * @return
	 */
	public Configuration<String, EntityRelationMapping> getEntityRelationMappingConfig(){
		return getConfiguration(ENTITY_RELATION_MAPPING_ID);
	}
	
	/**
	 * 设置实体类映射信息，会覆盖已有的
	 */
	public void setEntityRelationMapping(String entityClass, EntityRelationMapping erm){
		Configuration<String, EntityRelationMapping> config = getEntityRelationMappingConfig();
		config.put(entityClass, erm);
	}
	
	/**
	 * 添加实体类映射信息，不会覆盖已有的
	 */
	public void addEntityRelationMapping(String entityClass, EntityRelationMapping erm){
		Configuration<String, EntityRelationMapping> config = getEntityRelationMappingConfig();
		if (config != null && !config.containsKey(entityClass))
			config.put(entityClass, erm);
	}
	
	/**
	 * 获取实体类关系映射信息
	 */
	public EntityRelationMapping getEntityRelationMapping(Class<?> entityClass) {
		return getEntityRelationMapping(entityClass.getName());
	}
	
	/**
	 * 获取实体类关系映射信息
	 */
	public EntityRelationMapping getEntityRelationMapping(String entityClass) {
		Configuration<String, EntityRelationMapping> config = getConfiguration(ENTITY_RELATION_MAPPING_ID);
		if (config == null) return null;
		return config.get(entityClass);
	}
	
}
