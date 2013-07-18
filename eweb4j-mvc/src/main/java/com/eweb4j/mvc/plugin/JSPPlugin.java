package com.eweb4j.mvc.plugin;

import com.eweb4j.mvc.plugin.engine.JSPEngineImpl;
import com.eweb4j.mvc.view.TemplateEngine;

public class JSPPlugin extends AbstractTemplateEnginePlugin{

	private final static String engine_key = "jsp";
	
	@Override
	public String ID() {
		return "JSP-Plugin";
	}

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
