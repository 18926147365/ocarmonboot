package com.ocarmon.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletComponentScan;

import com.alibaba.druid.support.http.WebStatFilter;
import com.auth0.jwt.interfaces.Claim;

/**
 * @author 李浩铭
 * @since 2017年7月31日 下午3:43:16
 */
// druid过滤器.
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*", initParams = {
		// 忽略资源
		@WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*") })
public class DruidStatFilter extends WebStatFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest res, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) res;
		HttpServletResponse response = (HttpServletResponse) rep;
		request.setCharacterEncoding("UTF-8");
		
		response.setCharacterEncoding("UTF-8");
		

		super.doFilter(request, response, chain);
	}

	@Override
	public void destroy() {

	}

}