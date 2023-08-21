package com.halowing.lib.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.halowing.lib.string.StringUtility;
import com.halowing.lib.web.exception.DefaultWebApplicationException;



public class ApiKeyFilter implements Filter{
	
	private final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
	
	private final static String HEADER_NAME = "Api-key";

	private static final int HTTP_STATUS_UNAUTHORIZED_ERROR = 401;
	
	private final ApiKeyService service;
	
	private enum HttpMethod {
		HEAD, OPTIONS, TRACE, PATCH, GET, POST, PUT, DELETE;
	}
	
	public ApiKeyFilter(ApiKeyService service) {
		super();
		this.service = service;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		log.debug("Method = {}", httpRequest.getMethod());
		if(HttpMethod.OPTIONS.name().equals(httpRequest.getMethod())) {
			log.debug("OPTION");
		}else {
			Enumeration<String> headers = httpRequest.getHeaderNames();
			
			String headerName = null;
			String value = null;
			
			while(headers.hasMoreElements()) {
				headerName = headers.nextElement();
				value = httpRequest.getHeader(headerName);
				
				log.debug("header name = {}, \t value = {}", headerName, value);
				
				if(HEADER_NAME.equalsIgnoreCase(headerName))
				{
					break;
				}
				else
				{
					headerName = null;
					value = null;
				}
			}
			
			
			
			if(headerName == null )
				throw new DefaultWebApplicationException(HTTP_STATUS_UNAUTHORIZED_ERROR, " Api-key is not present ");
			
			if( StringUtility.isBlank(value)  )
				throw new DefaultWebApplicationException(HTTP_STATUS_UNAUTHORIZED_ERROR, "value of Api-key is empty.");
			
			if(!service.hasApiKey(value))
				throw new DefaultWebApplicationException(HTTP_STATUS_UNAUTHORIZED_ERROR, "It is not granted to access this system. Api-key="+value);
		}
		chain.doFilter(request, response);
	}

}
