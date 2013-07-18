package com.eweb4j.mvc.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模板引擎抽象类
 * @author vivi
 *
 */
public abstract class TemplateEngine {

	protected Map<String, String> paths = new HashMap<String, String>();
	protected String layout;
	protected String viewRelativePath;
	protected String viewAbsolutePath;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext servletContext;
	
	public abstract String getPath(String uri);
	
	public abstract void render(Map<String, Object> datas);
	
	public void render(String name, Object value){
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put(name, value);
		render(datas);
	}

	private void checkFile(String path) {
		File f = new File(viewAbsolutePath + path);
		if (!f.isFile())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " is not a file");
		
		if (!f.exists())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " does not exists");
		
	}
	
	public TemplateEngine target(String _path) {
		String path = _path.trim();
		if (!path.contains("->") && !path.contains(",")){
			String file = path;
			checkFile(file);
			this.paths.put("screen", file);
		} else{
			//处理形如 key1->value1,key2->value2
			//1.split by ,
			String[] pathStrs = path.split(",");
			//继续split by ->
			for (String ps : pathStrs){
				String str = ps.trim();
				String[] kv = str.split("->");
				String key = kv[0].trim();
				String value = kv[1].trim();
				checkFile(value);
				paths.put(key, value);
			}
		}
		
		return this;
	}
	
	public TemplateEngine layout(String _path){
		String path = _path.trim();
		checkFile(path);
		this.layout = path;
		return this;
	}
	
	public String getViewAbsolutePath() {
		return viewAbsolutePath;
	}

	public void setViewAbsolutePath(String viewAbsolutePath) {
		this.viewAbsolutePath = viewAbsolutePath;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getViewRelativePath() {
		return viewRelativePath;
	}

	public void setViewRelativePath(String viewRelativePath) {
		this.viewRelativePath = viewRelativePath;
	}

}
