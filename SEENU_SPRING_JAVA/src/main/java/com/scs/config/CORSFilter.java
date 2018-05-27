package com.scs.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class CORSFilter extends GenericFilterBean {

	@Autowired
	Environment env;

	public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse response = (HttpServletResponse) res;
		ServletContext servletContext = request.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		env = webApplicationContext.getBean(Environment.class);
		String path = req.getRequestURI().substring(req.getContextPath().length());
		String url = "/api/socket/info";
		System.out.println("path: "+ path);

		if (!url.contains(path)) {
			response.addHeader("Access-Control-Allow-Origin", env.getProperty("scs.allowed.client"));
			response.addHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
			response.addHeader("Access-Control-Max-Age", "3600");
			response.addHeader("Access-Control-Allow-Headers",
					"Content-Type, Access-Control-Allow-Headers, Authorization,X-Requested-With, X-Auth-Token");
			response.addHeader("Access-Control-Allow-Credentials", "true");

			if (("options").equalsIgnoreCase(req.getMethod())) {
				return;
			}
		}
		chain.doFilter(request, response);
	}

}