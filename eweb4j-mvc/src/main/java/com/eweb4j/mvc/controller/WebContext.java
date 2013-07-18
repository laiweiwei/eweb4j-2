package com.eweb4j.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eweb4j.mvc.view.TemplateEngine;

/**
 * @author vivi
 *
 */
public class WebContext {

	private ServletContext servletContext;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private PrintWriter writer;
	
	private TemplateEngine templateEngine;
	
	private String uri;
	
	private String httpMethod;

	private String rootPath;
	
	private String viewAbsolutePath;
	
	private String viewRelativePath;
	
    private static ThreadLocal<WebContext> threadLocal = new ThreadLocal<WebContext>();
	
	public static WebContext me(){
		return threadLocal.get();
	}

	public static WebContext init(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		WebContext context = new WebContext();
		context.setRequest(request);
		context.setResponse(response);
		context.setUri(parseURI(request));
		context.setHttpMethod(parseHttpMethod(request));
		
		threadLocal.set(context);
		return context;
	}
	
	private static String parseURI(HttpServletRequest request) throws Throwable {
		String uri = URLDecoder.decode(request.getRequestURI(), "utf-8");
		String contextPath = URLDecoder.decode(request.getContextPath(),"utf-8");

		if (contextPath != null && contextPath.trim().length() > 0)
			return uri.replace(contextPath, "");

		return uri;
	}
	
	private static String parseHttpMethod(HttpServletRequest request) {
		String reqMethod = request.getMethod();
		if (!"POST".equalsIgnoreCase(reqMethod))
			return reqMethod;

		String _method = request.getParameter("_method");
		if (_method == null)
			return reqMethod;

		if ("PUT".equalsIgnoreCase(_method.trim()))
			reqMethod = "PUT";
		else if ("DELETE".equalsIgnoreCase(_method.trim()))
			reqMethod = "DELETE";

		return reqMethod;
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getViewAbsolutePath() {
		return viewAbsolutePath;
	}

	public void setViewAbsolutePath(String viewAbsolutePath) {
		this.viewAbsolutePath = viewAbsolutePath;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public PrintWriter getWriter() throws IOException {
		if (writer == null)
			writer = response.getWriter();

		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public String getViewRelativePath() {
		return viewRelativePath;
	}

	public void setViewRelativePath(String viewRelativePath) {
		this.viewRelativePath = viewRelativePath;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}
	
}
