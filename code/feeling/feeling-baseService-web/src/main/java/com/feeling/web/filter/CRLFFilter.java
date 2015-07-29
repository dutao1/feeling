package com.feeling.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CRLFFilter implements Filter {
	
	@Override
	public void destroy() {

	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletResponse response = new CRLFFilterResponseWrapper((HttpServletResponse) res);
		chain.doFilter(req, response);
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {

	}
	
}
