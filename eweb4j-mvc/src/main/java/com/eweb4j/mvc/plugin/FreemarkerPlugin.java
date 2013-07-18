package com.eweb4j.mvc.plugin;

import java.io.File;

import com.eweb4j.mvc.plugin.engine.FreemarkerEngineImpl;
import com.eweb4j.mvc.view.TemplateEngine;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public class FreemarkerPlugin extends AbstractTemplateEnginePlugin{

	private final static String engine_key = "freemarker";
	private final Configuration cfg = new Configuration();
	
	@Override
	public String ID() {
		return "Freemarker-Plugin";
	}

	@Override
	public void init(String viewAbsolutePath) {
		try {
			// 指定模板从何处加载的数据源，这里设置成一个文件目录。
			cfg.setDirectoryForTemplateLoading(new File(viewAbsolutePath));
			// 指定模板如何检索数据模型
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("UTF-8");
		} catch (Throwable e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String engine_key() {
		return engine_key;
	}

	@Override
	public TemplateEngine templateEngine(){
		return new FreemarkerEngineImpl(cfg);
	}
	
}
