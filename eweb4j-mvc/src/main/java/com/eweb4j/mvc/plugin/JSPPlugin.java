package com.eweb4j.mvc.plugin;

import com.eweb4j.mvc.plugin.engine.JSPEngineImpl;
import com.eweb4j.mvc.view.TemplateEngine;

public class JSPPlugin extends AbstractTemplateEnginePlugin{

	private final static String engine_key = "jsp";
	
	@Override
	public void init(String viewAbsolutePath) {
	}
	
	@Override
	public TemplateEngine templateEngine(){
		return new JSPEngineImpl();
	}
	
	@Override
	public String engine_key() {
		return engine_key;
	}
}
