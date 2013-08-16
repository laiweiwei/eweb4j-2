package com.eweb4j.mvc.plugin;

import java.io.File;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.eweb4j.mvc.plugin.engine.VelocityEngineImpl;
import com.eweb4j.mvc.view.TemplateEngine;

public class VelocityPlugin extends AbstractTemplateEnginePlugin{

	private final static String engine_key = "velocity";
	private VelocityEngine ve = new VelocityEngine();
	
	@Override
	public void init(String viewAbsolutePath) {
		// 初始化Velocity模板引擎
		File viewsDir = new File(viewAbsolutePath);
        Properties p = new Properties();
        p.setProperty("resource.loader", "file");
        p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        p.setProperty("file.resource.loader.path", viewsDir.getAbsolutePath());
        p.setProperty("file.resource.loader.cache", "true");
        p.setProperty("file.resource.loader.modificationCheckInterval", "2");
        p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        ve.init(p);
	}

	@Override
	public TemplateEngine templateEngine(){
		return new VelocityEngineImpl(ve);
	}
	
	@Override
	public String engine_key() {
		return engine_key;
	}

}
