package com.eweb4j.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.ConfigurationFactoryImpl;
import com.eweb4j.core.configuration.xml.PojoBean;
import com.eweb4j.core.ioc.EWeb4JIOC;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.plugin.PluginManagerImpl;

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
		Configuration<String, String> pluginConfig = configFactory.getPluginConfig();
		for (Iterator<Entry<String, String>> it = pluginConfig.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, String> e = it.next();
			String pluginID = e.getKey();
			String pluginClass = e.getValue(); 
			try {
				@SuppressWarnings("unchecked")
				Class<Plugin> cls = (Class<Plugin>) Thread.currentThread().getContextClassLoader().loadClass(pluginClass);
				Plugin _plugin = cls.newInstance();
				this.eweb4j.addPlugin(pluginID, _plugin);
			} catch (Throwable exp) {
				throw new RuntimeException(exp);
			}
		}
		
		//设置IOC容器
		//FIXME 重构，去掉EWeb4J和EWeb4JIOC的循环引用
		Configuration<String, PojoBean> iocConfig = eweb4j.getConfigFactory().getIocConfig();
		if (iocConfig != null){
			this.eweb4j.setIOC(new EWeb4JIOC(this.eweb4j));
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

	@Deprecated
	public EWeb4J startup() {
		return this;
	}

	public EWeb4J shutdown() {
		this.eweb4j.shutdown();
		return this;
	}

	public void setIOC(IOC ioc) {
		this.eweb4j.setIOC(ioc);
	}

	public IOC getIOC() {
		return this.eweb4j.getIOC();
	}

	public void addPlugin(String id, Plugin plugin) {
		this.eweb4j.addPlugin(id, plugin);
	}

}
