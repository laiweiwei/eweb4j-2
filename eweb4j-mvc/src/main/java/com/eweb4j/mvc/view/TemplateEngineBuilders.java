package com.eweb4j.mvc.view;

import java.util.HashMap;
import java.util.Map;

/**
 * 负责创建模板引擎实例的实现类注册列表
 * @author vivi
 *
 */
public class TemplateEngineBuilders {

	public final static Map<String, TemplateEngineBuilder> builders = new HashMap<String, TemplateEngineBuilder>();
	
	public final static String current = "_current_";
	
	public final static TemplateEngineBuilder current(){
		return builders.get(current);
	}
	
	public final static TemplateEngineBuilder get(String engine){
		return builders.get(engine);
	}
	
	public final static void putCurrent(TemplateEngineBuilder builder){
		builders.put(current, builder);
	}
	
	public final static void remove(){
		builders.remove(current);
	}
}
