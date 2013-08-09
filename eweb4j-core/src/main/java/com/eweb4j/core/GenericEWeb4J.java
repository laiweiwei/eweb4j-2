package com.eweb4j.core;

import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.toolbox.Toolbox;


/**
 * 普通的EWeb4J实现
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class GenericEWeb4J implements EWeb4J{
	
	private ConfigurationFactory configFactory = null;
	private PluginManager pluginManager = null;
	
	private List<Listener> listeners = new ArrayList<Listener>();
	private List<Plugin> plugins = new ArrayList<Plugin>();
	
	public GenericEWeb4J(ConfigurationFactory configFactory, PluginManager pluginManager) {
		this.configFactory = configFactory;
		this.pluginManager = pluginManager;
	}
	
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
	public ConfigurationFactory getConfigFactory() {
		return this.configFactory;
	}

	public void addListener(Listener listener){
		this.listeners.add(listener);
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}
	
	public void addPlugin(Plugin plugin) {
		this.plugins.add(plugin);
	}
	
	public List<Plugin> getPlugins() {
		return plugins;
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 */
	public final GenericEWeb4J startup() {
		for (Plugin plugin : this.plugins) {
			this.pluginManager.install(plugin, this);
		}
		
		List<String> listenerClasses = this.configFactory.getListeners();
		if (listenerClasses != null) {
			for (String listenerClass : listenerClasses) {
				try {
					@SuppressWarnings("unchecked")
					Class<EWeb4J.Listener> clazz = (Class<EWeb4J.Listener>) Thread.currentThread().getContextClassLoader().loadClass(listenerClass);
					EWeb4J.Listener listener = clazz.newInstance();
					this.addListener(listener);
				} catch (Throwable e) {
				}
			}
		}
		
		for (Listener listener : this.listeners) 
			listener.onStartup(this);
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 */
	public final GenericEWeb4J shutdown() {
		for (Listener listener : this.listeners) 
			listener.onShutdown(this);
		
		this.pluginManager.stopAll();
		return this;
	}

	/**
	 * 从IOC容器里获取一个具有某种特性的对象，比如DAO对象
	 * @param feature ioc配置的pojo id
	 * @param args 构造器参数
	 */
	@SuppressWarnings("unchecked")
	public <T extends Toolbox> T getToolbox(String toolName, Object... args) {
		Configuration<String, Toolbox> toolboxConfig = this.configFactory.getToolbox();
		Toolbox toolbox = toolboxConfig.get(toolName);
		toolbox.init(this, args);
		return (T) toolbox;
	}
	
}
