package com.eweb4j.mvc.plugin.engine;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eweb4j.mvc.view.TemplateEngine;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerEngineImpl extends TemplateEngine{
	
	private Configuration cfg = null;
	
	public FreemarkerEngineImpl(Configuration cfg){
		this.cfg = cfg;
	}
	
	public String getPath(String uri){
		String relativePath  = uri + ".ftl";
		final String dir = viewAbsolutePath+File.separator+uri;
		File ftl = new File(dir+".ftl");
		if (!ftl.exists()){
			System.out.println("----------- FILE_NOT_FOUND -----------");
			System.out.println("----------- "+ftl.getAbsolutePath()+" -----------");
			ftl = new File(dir);
			if (!ftl.exists()) {
				System.out.println("----------- DIR_NOT_FOUND -----------");
				System.out.println("----------- "+dir+" -----------");
				return null;
			}
			//找文件夹下的index.ftl
			relativePath = uri + File.separator + "index.ftl";
			final String indexftl = dir + File.separator + "index.ftl";
			ftl = new File(indexftl);
			if (!ftl.exists()) {
				System.out.println("----------- INDEX_FILE_NOT_FOUND -----------");
				System.out.println("----------- "+indexftl+" -----------");
				return null;
			}
		}
		if (!ftl.isFile()){
			System.out.println("----------- INDEX_FILE_IS_NOT_FILE -----------");
			System.out.println("----------- "+ftl.getAbsolutePath()+" -----------");
			return null;
		}
		
		return relativePath;
	}
	
	public void render(Map<String, Object> datas) {
		if (datas == null)
			datas = new HashMap<String, Object>();
		
		try {
			String ftlPath = paths.get("screen");
			
			// 将环境变量和输出部分结合
			if (this.layout != null){
				for (Iterator<Entry<String, String>> it = this.paths.entrySet().iterator(); it.hasNext(); ){
					Entry<String, String> e = it.next();
					String paramName = e.getKey();
					String path = e.getValue();
					
					StringWriter w = new StringWriter();
					Template template = cfg.getTemplate(path);
					template.setEncoding("UTF-8");
					template.process(datas, w);
					String screenContent = w.toString();
					
					datas.put(paramName, screenContent);
				}
				
				ftlPath = layout;
			}
			
			Template template = cfg.getTemplate(ftlPath);
			template.setEncoding("UTF-8");
			template.process(datas, response.getWriter());
		} catch (Throwable e){
			e.printStackTrace();
		}
	}

}
