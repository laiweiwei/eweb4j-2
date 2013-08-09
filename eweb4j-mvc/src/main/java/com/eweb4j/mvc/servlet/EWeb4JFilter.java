package com.eweb4j.mvc.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.SimpleEWeb4J;
import com.eweb4j.mvc.controller.WebContext;
import com.eweb4j.mvc.view.TemplateEngine;
import com.eweb4j.mvc.view.TemplateEngineBuilder;
import com.eweb4j.mvc.view.TemplateEngineBuilders;
import com.eweb4j.utils.CommonUtil;

public class EWeb4JFilter implements Filter{

	private FilterConfig filterConfig = null;
	private ServletContext servletContext = null;
	private EWeb4J eweb4j = null;
	
	private final static String class_path ;
	static {
		class_path = EWeb4JFilter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println("----------- CLASS_PATH -----------");
		System.out.println("----------- "+class_path+" -----------");
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.servletContext = this.filterConfig.getServletContext();
		String xml = this.filterConfig.getInitParameter("config-xml");
		
		//构建框架实例
		this.eweb4j = new SimpleEWeb4J(xml);
	}
	
	public void destroy() {
		//关闭框架
		eweb4j.shutdown();
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			//初始化WebContext
			WebContext ctx = WebContext.init(request, response);
			ctx.setServletContext(this.servletContext);
			ctx.setRootPath(this.eweb4j.getConfigFactory().getAbsolutePathOfRoot());
			ctx.setViewRelativePath(this.eweb4j.getConfigFactory().getRelativePathOfView());
			ctx.setViewAbsolutePath(this.eweb4j.getConfigFactory().getAbsolutePathOfView());
			//FIXME:考虑多级域名对应不同视图文件夹的功能
			final String uri = ctx.getUri();
			System.out.println("----------- URI -----------");
			System.out.println("----------- "+uri+" -----------");
			//映射URI到视图文件
			/*
			 * 映射规则：
			 * 1.先找跟URI一样的命名的File存在,若是文件，直接渲染，若是文件夹，找里面的index文件，若没有进入下一步
			 * 2.找绑定了URI的Controller.method,若有，执行它，否则交给下一个filter处理
			 */
			
			//获取当前的模板引擎建造者
			TemplateEngineBuilder engineBuilder = TemplateEngineBuilders.current();
			
			//创建模板引擎实例
			TemplateEngine engine = engineBuilder.build();
			ctx.setTemplateEngine(engine);
			
			//设置一些必要的参数
			engine.setServletContext(servletContext);
			engine.setRequest(request);
			engine.setResponse(response);
			engine.setViewRelativePath(ctx.getViewRelativePath());
			engine.setViewAbsolutePath(ctx.getViewAbsolutePath());
			
			//根据映射规则找到对应的模板文件路径
			final String path = engine.getPath(uri);
			if (path != null && path.trim().length() > 0){
				//若找到了路径，渲染该模板内容作为HTTP响应
				System.out.println("----------- VIEW_FILE_FOUND -----------");
				System.out.println("----------- "+path+" -----------");
				Map<String, Object> datas = new HashMap<String, Object>();
				datas.put("ctx", ctx);
				datas.put("request", request);
				datas.put("response", response);
				datas.put("request_uri", uri);
				
				//渲染模板，并输出到HTTP响应流
				engine.target(path).render(datas);
				return;
			}
			
			//映射URI到Pojo
			/*
			 *  /users/hello -> {controller_package}+Users.class#hello
			 */
			String[] uris = uri.split("/");
			if (uris.length == 3) {
				String controllerClassName = "controller."+CommonUtil.toUpCaseFirst(uris[1]);
				System.out.println("----------- CONTROLLER_CLASS_NAME -----------");
				System.out.println("----------- "+controllerClassName+" -----------");
				String methodName = uris[2];
				Class<?> controllerClass = null;
				Object controller = null;
				Method method = null;
				try {
					controllerClass = Thread.currentThread().getContextClassLoader().loadClass(controllerClassName);
				} catch (Throwable e){
					e.printStackTrace();
				}
				
				if (controllerClass != null) {
					System.out.println("----------- CONTROLLER_CLASS -----------");
					System.out.println("----------- "+controllerClass+" -----------");
					try {
						controller = controllerClass.newInstance();
					} catch (Throwable e){
						e.printStackTrace();
					}
					if (controller != null) {
						System.out.println("----------- CONTROLLER -----------");
						System.out.println("----------- "+controller+" -----------");
						try {
							method = controllerClass.getMethod(methodName);
						} catch (Throwable e){
							e.printStackTrace();
						}
					}
				}
				
				Object output = null;
				if (method != null){
					System.out.println("----------- CONTROLLER_METHOD -----------");
					System.out.println("----------- "+method+" -----------");
					try {
						output = method.invoke(controller);
					} catch (Throwable e){
						e.printStackTrace();
					}
				}
				
				if (output != null) {
					System.out.println("----------- METHOD_OUTPUT -----------");
					System.out.println("----------- "+output+" -----------");
					ctx.getWriter().print(output);
					return ;
				}
			}
			//继续往下执行URL
			chain.doFilter(req, res);
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			
		}
	}

	public static void main(String[] args){
		System.out.println("/users/hello".split("/")[1]);
	}
	
}
