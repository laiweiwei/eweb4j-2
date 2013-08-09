package com.eweb4j.core;

import java.util.List;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.ConfigurationFactoryImpl;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.plugin.PluginManagerImpl;
import com.eweb4j.core.toolbox.Toolbox;

/**
 * 单纯的EWeb4J实现
 * @author vivi
 *
 */
public class SimpleEWeb4J implements EWeb4J{

	private EWeb4J eweb4j = null;
	
	public SimpleEWeb4J(String xml){
		//构建配置工厂实例
		final ConfigurationFactory configFactory = new ConfigurationFactoryImpl(xml);
		
		//构建插件管理器
		final PluginManager pluginManager = new PluginManagerImpl();
		
		//构建框架实例
		this.eweb4j = new GenericEWeb4J(configFactory, pluginManager);
		
		//添加插件
		List<String> pluginClasses = configFactory.getPlugins();
		for (String pluginClass : pluginClasses) {
			try {
				@SuppressWarnings("unchecked")
				Class<Plugin> cls = (Class<Plugin>) Thread.currentThread().getContextClassLoader().loadClass(pluginClass);
				Plugin _plugin = cls.newInstance();
				this.eweb4j.addPlugin(_plugin);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		
		//准备完毕，启动框架
		this.eweb4j.startup();
	}
	
	public PluginManager getPluginManager() {
		return this.eweb4j.getPluginManager();
	}

	public ConfigurationFactory getConfigFactory() {
		return this.eweb4j.getConfigFactory();
	}

	@Deprecated
	public void addListener(Listener listener) {
	}

	public List<Listener> getListeners() {
		return this.eweb4j.getListeners();
	}

	@Deprecated
	public void addPlugin(Plugin plugin) {
	}

	public List<Plugin> getPlugins() {
		return this.eweb4j.getPlugins();
	}

	@Deprecated
	public EWeb4J startup() {
		return this;
	}

	public EWeb4J shutdown() {
		this.eweb4j.shutdown();
		return this;
	}

	/**
	 * 从IOC容器里获取一个具有某种特性的对象，比如DAO对象
	 * @param feature ioc配置的pojo id
	 * @param args 构造器参数
	 */
	public <T extends Toolbox> T getToolbox(String toolName, Object... args) {
		return this.eweb4j.getToolbox(toolName, args);
	}

}
