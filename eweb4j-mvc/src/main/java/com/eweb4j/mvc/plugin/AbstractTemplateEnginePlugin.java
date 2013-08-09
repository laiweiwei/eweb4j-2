package com.eweb4j.mvc.plugin;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.mvc.view.TemplateEngine;
import com.eweb4j.mvc.view.TemplateEngineBuilder;
import com.eweb4j.mvc.view.TemplateEngineBuilders;

/**
 * 模板引擎插件 抽象类
 * @author vivi
 *
 */
public abstract class AbstractTemplateEnginePlugin extends Plugin{

	public abstract String engine_key();
	
	public abstract void init(String absolutePathOfView);
	
	public abstract TemplateEngine templateEngine();
	
	@Override
	public String name() {
		return ID();
	}
	
	@Override
	public String provider() {
		return "l.weiwei@163.com";
	}
	
	@Override
	public void init(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		String absolutePathOfView = configFactory.getAbsolutePathOfView();
		init(absolutePathOfView);
	}
	
	@Override
	public void start(){
		TemplateEngineBuilder builder = new TemplateEngineBuilder() {
			public TemplateEngine build() {
				return templateEngine();
			}
		};
		
		TemplateEngineBuilders.builders.put(engine_key(), builder);
		TemplateEngineBuilders.putCurrent(builder);
	}
	
	@Override
	public void stop() {
		TemplateEngineBuilders.builders.remove(engine_key());
		TemplateEngineBuilders.remove();
	}
	
}
