package com.eweb4j.core;

import com.eweb4j.core.plugin.Plugins;
import com.eweb4j.core.plugin.PluginsMgr;


/**
 * EWeb4J 启动类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class EWeb4J {

	public final Plugins pluginMgr = new PluginsMgr();
	
	public static EWeb4J me() {
		return new EWeb4J();
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 * @param listener
	 */
	public final EWeb4J startup(final Listener listener) {
		listener.onStartup(pluginMgr);
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 * @param listener
	 */
	public final EWeb4J shutdown(final Listener listener) {
		listener.onShutdown(pluginMgr);
		
		return this;
	}
	
	public static interface Listener {
		
		public void onStartup(Plugins plugins);
		
		public void onShutdown(Plugins plugins);
		
	}
}
