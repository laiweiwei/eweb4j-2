package com.eweb4j.core.plugin;

import com.eweb4j.core.configurator.Storage;

/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:39:03
 */
public class PluginManagerImpl extends PluginManager{
	
	/**
	 * 插件仓库
	 */
	protected Storage<String, Plugin> pluginStorage = null;
	
	/**
	 * 配置仓库
	 */
	protected Storage<String, Object> configStorage = null; 
	
	/**
	 * 无参构造器
	 */
	public PluginManagerImpl(){}
	
	/**
	 * 有参构造器
	 * @param pluginsStorage 插件仓库
	 * @param config 配置仓库
	 */
	public PluginManagerImpl(Storage<String, Plugin> pluginStorage, Storage<String, Object> configStorage){
		this.setPluginStorage(pluginStorage);
		this.setConfigStorage(configStorage);
	}
	
	/**
	 * 获取插件仓库实例
	 */
	public Storage<String, Plugin> getPluginStorage() {
		return pluginStorage;
	}

	/**
	 * 注入插件仓库实例
	 * @param pluginStorage
	 */
	public void setPluginStorage(Storage<String, Plugin> pluginStorage) {
		this.pluginStorage = pluginStorage;
	}

	/**
	 * 获取配置仓库实例
	 */
	public Storage<String, Object> getConfigStorage() {
		return configStorage;
	}

	/**
	 * 注入配置仓库实例
	 * @param configStorage
	 */
	public void setConfigStorage(Storage<String, Object> configStorage) {
		this.configStorage = configStorage;
	}

	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public Plugin getPlugin(String id) {
		return this.pluginStorage == null ? null : this.pluginStorage.get(id);
	}
	
	/**
	 * 安装插件
	 */
	public boolean install(Plugin plugin) {
		if (this.pluginStorage.containsKey(plugin.ID()))
			return true;
		
//		plugin.setConfigs(super.configs);
		
		//初始化插件
		try {
			plugin.init(this.configStorage);
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
		this.pluginStorage.put(plugin.ID(), plugin);
		
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
		this.pluginStorage.remove(plugin.ID());
		
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
		while (this.pluginStorage.hasNext()) {
			Plugin plugin = this.pluginStorage.next();
			uninstall(plugin);
		}
		
		return true;
	}
	
}
