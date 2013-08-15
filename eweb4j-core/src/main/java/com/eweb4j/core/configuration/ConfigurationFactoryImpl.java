package com.eweb4j.core.configuration;

import static com.eweb4j.core.EWeb4J.Constants.Configurations.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.EWeb4J.Constants.Configurations.Types;
import com.eweb4j.core.configuration.xml.ConfigurationXmlBean;
import com.eweb4j.core.configuration.xml.EWeb4JXmlBean;
import com.eweb4j.core.configuration.xml.FileXmlBean;
import com.eweb4j.core.configuration.xml.PropertyXmlBean;

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
		EWeb4JXmlBean eweb4j = serializer.read(EWeb4JXmlBean.class, xml);
		if (eweb4j == null) return null;
		
		List<ConfigurationXmlBean> configs = eweb4j.getConfigurations();
		for (ConfigurationXmlBean config : configs) {
			Configuration<String, String> configuration = null;
			String id = config.getId();
			//只要base的配置
			if (!BASE_ID.equals(id)) continue;
			List<PropertyXmlBean> propertiesBean = config.getProperties();
			if (propertiesBean == null || propertiesBean.isEmpty()) continue;
			configuration = new MapConfiguration<String, String>(propertiesBean.size());
			for (PropertyXmlBean p : propertiesBean) {
				//若不开启，跳过
				if (0 == p.getEnabled()) continue;
				
				configuration.put(p.getName(), p.getValue());
			}
			
			return configuration;
		}
		
		return null;
	}
	
	private void storeProperties(Configuration<String, Object> configuration, List<PropertyXmlBean> propertiesBean){
		Map<String, StringBuilder> arrayMap = new HashMap<String, StringBuilder>();
		for (PropertyXmlBean p : propertiesBean) {
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
		EWeb4JXmlBean eweb4j = serializer.read(EWeb4JXmlBean.class, xml);
		if (eweb4j == null) return ;
	
		List<ConfigurationXmlBean> configs = eweb4j.getConfigurations();
		for (ConfigurationXmlBean config : configs) {
			Configuration<String, Object> configuration = null;
			String id = config.getId();
			String type = config.getType();
			List<PropertyXmlBean> propertyBeans = config.getProperties();
			//根据不同的类型处理不同配置信息的获取来源
			if (Types.PROPERTIES.equals(type)) {
				List<FileXmlBean> filePaths = config.getFiles();
				configuration = new MapConfiguration<String, Object>();
				if (filePaths != null && !filePaths.isEmpty()) {
					for (FileXmlBean filePathBean : filePaths) {
						//若不开启，跳过
						if (0 == filePathBean.getEnabled()) continue;
						String filePath = filePathBean.getPath();
						File f = new File(filePath);
						if (!f.exists()) throw new Exception("file->"+f.getAbsolutePath()+" not found");
			        	if (!f.isFile()) throw new Exception("file->"+f.getAbsolutePath()+" is not a file");
						//读取properties文件
			        	configuration.getMap().putAll(new PropertiesConfiguration(filePath).getMap());
					}
				}
			} else if (Types.XML.equals(type)){
				List<FileXmlBean> filePaths = config.getFiles();
				if (filePaths != null && !filePaths.isEmpty()) {
					String configClass = config.getHolder();
					@SuppressWarnings("unchecked")
					Class<XMLConfiguration<String, Object>> cls = (Class<XMLConfiguration<String, Object>>) Thread.currentThread().getContextClassLoader().loadClass(configClass);
					configuration = cls.newInstance();
					XMLConfiguration<String, Object> c = (XMLConfiguration<String, Object>)configuration;
					for (FileXmlBean filePathBean : filePaths) {
						//若不开启，跳过
						if (0 == filePathBean.getEnabled()) continue;
						String filePath = filePathBean.getPath();
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
	private void init(String xml){
		if (xml == null || xml.trim().length() == 0) 
			xml = EWeb4J.Constants.config_xml;
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
        		context = baseConfig.getMap();
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
	
	public Configuration<String, ?> addConfiguration(String id, Configuration<String, ?> configuration) {
		return this.configs.put(id, configuration);
	}

	
	/**
	 * 获取base配置信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getBaseConfig(){
		return (Configuration<String, V>) configs.get(BASE_ID);
	}
	
	/**
	 * 获取jdbc配置信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getJdbcConfig(){
		return (Configuration<String, V>) configs.get(JDBC_ID);
	}
	
	@SuppressWarnings("unchecked")
	public <V> Configuration<String, V> getIOCConfig(){
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
	
	private List<String> getListString(String id, String key){
		return getListString(id, key, ",");
	}
	private List<String> getListString(String id, String key, String split){
		Configuration<String, ?> config = this.configs.get(id);
		if (config == null) return null;
		
		return config.getListString(key, split);
	}
	
	private String getString(String id, String key){
		return getString(id, key, null);
	}
	
	private String getString(String id, String key, String defaultVal){
		Configuration<String, ?> config = this.configs.get(id);
		if (config == null) return null;
		return config.getString(key, defaultVal);
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
		Configuration<String, ?> config = this.configs.get(DATA_SOURCE_ID);
		if (config == null) return null;
		return (DataSource) config.get(dsName);
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
	
	public List<String> getPlugins() {
		return this.getListString(PLUGIN_ID, "class");
	}
	
	/**
	 * 获取JPA注解配置信息
	 */
	public <T> T getJPA(Class<?> entityClass) {
		return getJPA(entityClass.getName());
	}
	
	/**
	 * 获取JPA注解配置信息
	 */
	@SuppressWarnings("unchecked")
	public <T> T getJPA(String entityClass) {
		Configuration<String, ?> config = getConfiguration(JPA_ID);
		if (config == null) return null;
		return (T) config.get(entityClass);
	}

}
