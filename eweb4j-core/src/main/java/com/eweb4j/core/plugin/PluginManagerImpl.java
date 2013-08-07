package com.eweb4j.core.plugin;

import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;

/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:39:03
 */
public class PluginManagerImpl extends PluginManager{
	
	/**
	 * 插件仓库
	 */
	protected Configuration<String, Plugin> plugins = null;
	
	/**
	 * 配置工厂实例
	 */
	protected ConfigurationFactory configurationFactory = null; 
	
	/**
	 * 无参构造器
	 */
	public PluginManagerImpl(){}
	
	/**
	 * 有参构造器
	 * @param pluginsStorage 插件仓库
	 * @param config 配置工厂实例
	 */
	public PluginManagerImpl(Configuration<String, Plugin> plugins, ConfigurationFactory configFactory){
		this.setPlugins(plugins);
		this.setConfigurationFactory(configFactory);
	}
	
	/**
	 * 获取插件仓库实例
	 */
	public Configuration<String, Plugin> getPlugins() {
		return plugins;
	}

	/**
	 * 注入插件仓库实例
	 * @param plugins
	 */
	public void setPlugins(Configuration<String, Plugin> plugins) {
		this.plugins = plugins;
	}

	/**
	 * 获取配置仓库实例
	 */
	public ConfigurationFactory getConfigurationFactory() {
		return this.configurationFactory;
	}

	/**
	 * 注入配置仓库实例
	 * @param configs
	 */
	public void setConfigurationFactory(ConfigurationFactory configurationFactory) {
		this.configurationFactory = configurationFactory;
	}

	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public Plugin getPlugin(String id) {
		return this.plugins == null ? null : this.plugins.get(id);
	}
	
	/**
	 * 安装插件
	 */
	public boolean install(Plugin plugin) {
		if (this.plugins.containsKey(plugin.ID()))
			return true;
		
//		plugin.setConfigs(super.configs);
		
		//初始化插件
		try {
			plugin.init(this.configurationFactory);
		} catch (Throwable e) {
			throw new RuntimeException("plugin->" + plugin.ID() + " init failed.", e);
		}
		
		//启动插件
		try {
			plugin.start();
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + plugin.ID() + " start failed.", e);
		}
		
		//注册插件ID到仓库中
		this.plugins.put(plugin.ID(), plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" install success.");
		
		return true;
	}
	
	/**
	 * 卸载插件
	 */
	public boolean uninstall(Plugin plugin) {
		//先关闭插件
		try {
			plugin.stop();
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + plugin.ID() + " stop failed.", e);
		}
		
		//从仓库中去除
		this.plugins.remove(plugin.ID());
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" uninstall success.");
		
		return true;
	}
	
	/**
	 * 升级插件 
	 */
	public boolean upgrade(Plugin plugin) {
		//先卸载插件
		uninstall(plugin);
		//然后安装插件
		install(plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" upgrade success.");
		
		return true;
	}

	/**
	 * 停止所有插件
	 */
	public boolean stopAll() {
		while (this.plugins.hasNext()) {
			Plugin plugin = this.plugins.next();
			uninstall(plugin);
		}
		
		return true;
	}
	
}
