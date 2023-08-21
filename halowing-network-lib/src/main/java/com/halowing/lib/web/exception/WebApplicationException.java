package com.halowing.lib.web.exception;

import com.halowing.lib.exception.ApplicationException;

public abstract class WebApplicationException extends ApplicationException {
	
	private static final long serialVersionUID = -4941203247664628241L;
	
	public static final int    HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
	public static final String HTTP_STATUS_INTERNAL_SERVER_ERROR_CODE = "http.status.internal_server_error"; 
	public static final String HTTP_STATUS_INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server error."; 

	public WebApplicationException(String message) {
		super(message);
	}
	
	public WebApplicationException(String message, Throwable t) {
		super(message, t);
	}

	public WebApplicationException(Throwable t) {
		super(t);
	}
	
	public abstract int getHttpStatus();
	
}
