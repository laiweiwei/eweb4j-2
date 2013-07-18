package com.eweb4j.mvc.plugin.engine;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eweb4j.mvc.view.TemplateEngine;

public class JSPEngineImpl extends TemplateEngine{
	
	public JSPEngineImpl(){
		System.out.println("...................JSP Engine initialize..............");
	}
	
	public String getPath(String uri){
		String relativePath  = uri + ".jsp";
		final String fileName = super.viewAbsolutePath+File.separator + relativePath;
		//优先级， 1.jsp 2.vm 3.ftl 4.html
		File jsp = new File(fileName);
		if (!jsp.exists()){
			System.out.println("----------- FILE_NOT_FOUND -----------");
			System.out.println("----------- "+fileName+" -----------");
			final String dir = super.viewAbsolutePath+File.separator+uri;
			jsp = new File(dir);
			if (!jsp.exists()) {
				System.out.println("----------- DIR_NOT_FOUND -----------");
				System.out.println("----------- "+dir+" -----------");
				return null;
			}
			//找文件夹下的index.jsp
			relativePath = uri + File.separator + "index.jsp";
			final String indexJsp = dir + File.separator + "index.jsp";
			jsp = new File(indexJsp);
			if (!jsp.exists()) {
				System.out.println("----------- INDEX_FILE_NOT_FOUND -----------");
				System.out.println("----------- "+indexJsp+" -----------");
				return null;
			}
		}
		if (!jsp.isFile()){
			System.out.println("----------- INDEX_FILE_IS_NOT_FILE -----------");
			System.out.println("----------- "+jsp.getAbsolutePath()+" -----------");
			return null;
		}
			
		return relativePath;
	}
	
	public void render(Map<String, Object> datas) {
		try {
			if (datas != null) {
				for (Iterator<Entry<String, Object>> it = datas.entrySet().iterator(); it.hasNext(); ) {
					Entry<String, Object> entry = it.next();
					String key = entry.getKey();
					Object val = entry.getValue();
					if (key != null && val != null)
						request.setAttribute(key, val);
				}
			}

			if(layout != null){
				for (Iterator<Entry<String, String>> it = this.paths.entrySet().iterator(); it.hasNext(); ){
					Entry<String, String> e = it.next();
					String paramName = e.getKey();
					String path = e.getValue();
					BufferedResponse my_res = new BufferedResponse(response);
					servletContext.getRequestDispatcher(super.viewAbsolutePath + path).include(request, my_res);
					String screenContent = my_res.getScreenContent();
					request.setAttribute(paramName, screenContent);
				}
				
				servletContext.getRequestDispatcher(super.viewRelativePath + layout).forward(request, response);
			} else {
				String defaultPath = paths.get("screen");
				servletContext.getRequestDispatcher(super.viewRelativePath + defaultPath).forward(request, response);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
