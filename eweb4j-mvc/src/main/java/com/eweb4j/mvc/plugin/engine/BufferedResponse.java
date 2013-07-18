package com.eweb4j.mvc.plugin.engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Response封装
 * @author Winter Lau
 *
 */
class BufferedResponse extends HttpServletResponseWrapper {

	StringWriter sout;
	PrintWriter pout;
	
	public BufferedResponse(HttpServletResponse res) {
		super(res);
		sout = new StringWriter();
		pout = new PrintWriter(sout);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return pout;
	}

	protected String getScreenContent() {
		return sout.toString();
	}
}