package com.eweb4j.mvc.plugin.engine;

import java.io.File;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.eweb4j.mvc.view.TemplateEngine;

/**
 * Velocity模板渲染实现类。
 * @author weiwei
 *
 */
public class VelocityEngineImpl extends TemplateEngine {
	
	private VelocityEngine ve = null;
	
	public VelocityEngineImpl(VelocityEngine ve) {
		this.ve = ve;
	}
	
	public String getPath(String uri){
		String relativePath  = uri + ".vm";
		final String dir = super.viewAbsolutePath+File.separator+uri;
		File vm = new File(dir+".vm");
		if (!vm.exists()){
			System.out.println("----------- FILE_NOT_FOUND -----------");
			System.out.println("----------- "+vm.getAbsolutePath()+" -----------");
			vm = new File(dir);
			if (!vm.exists()) {
				System.out.println("----------- DIR_NOT_FOUND -----------");
				System.out.println("----------- "+dir+" -----------");
				return null;
			}
			//找文件夹下的index.vm
			relativePath = uri + File.separator + "index.vm";
			final String indexVm = dir + File.separator + "index.vm";
			vm = new File(indexVm);
			if (!vm.exists()) {
				System.out.println("----------- INDEX_FILE_NOT_FOUND -----------");
				System.out.println("----------- "+indexVm+" -----------");
				return null;
			}
		}
		if (!vm.isFile()){
			System.out.println("----------- INDEX_FILE_IS_NOT_FILE -----------");
			System.out.println("----------- "+vm.getAbsolutePath()+" -----------");
			return null;
		}
		
		return relativePath;
	}
	
	public void render(Map<String, Object> datas) {
		VelocityContext context = new VelocityContext();
		if (datas != null) {
			for (Iterator<Entry<String, Object>> it = datas.entrySet().iterator(); it.hasNext(); ){
				Entry<String, Object> e = it.next();
				context.put(e.getKey(), e.getValue());
			}
		}
		
		String tplPath = paths.get("screen");
		
		// 将环境变量和输出部分结合
		if (this.layout != null){
			for (Iterator<Entry<String, String>> it = this.paths.entrySet().iterator(); it.hasNext(); ){
				Entry<String, String> e = it.next();
				String paramName = e.getKey();
				String path = e.getValue();
				
				StringWriter w = new StringWriter();
				ve.getTemplate(path).merge(context, w);
				String screenContent = w.toString();
				context.put(paramName, screenContent);
			}
			
			tplPath = layout;
		}
		
		try {
			ve.getTemplate(tplPath).merge(context, response.getWriter());
		} catch (Throwable e){
			e.printStackTrace();
		}
	}

}
