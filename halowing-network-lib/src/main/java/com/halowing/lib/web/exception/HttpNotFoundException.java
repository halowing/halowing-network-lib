package com.halowing.lib.web.exception;

public class HttpNotFoundException extends SimpleWebApplicationException {

	private static final long serialVersionUID = -4391515543765150039L;
	
	public static final int    HTTP_STATUS_NOT_FOUND_ERROR = 404;
	public static final String HTTP_STATUS_NOT_FOUND_ERROR_CODE = "http.status.not_found_error"; 
	public static final String HTTP_STATUS_NOT_FOUND_ERROR_MESSAGE = "request uri not founded."; 

	public HttpNotFoundException() {
		super(HTTP_STATUS_NOT_FOUND_ERROR,HTTP_STATUS_NOT_FOUND_ERROR_CODE);
	}
	
	public HttpNotFoundException(String args) {
		super(HTTP_STATUS_NOT_FOUND_ERROR, HTTP_STATUS_NOT_FOUND_ERROR_CODE, args);
	}
	
	public HttpNotFoundException(String errorCode, String args) {
		super(HTTP_STATUS_NOT_FOUND_ERROR, errorCode, args);
	}
}
