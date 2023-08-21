package com.halowing.lib.web.exception;

public class DefaultWebApplicationException extends RuntimeException  {

	private static final long serialVersionUID = 6630247127070485289L;
	
	private final int httpStatus;
	
	public DefaultWebApplicationException() {
		super(WebApplicationException.HTTP_STATUS_INTERNAL_SERVER_ERROR_MESSAGE);
		this.httpStatus = WebApplicationException.HTTP_STATUS_INTERNAL_SERVER_ERROR;
	}
	
	public DefaultWebApplicationException(Throwable e) {
		super(e);
		this.httpStatus = WebApplicationException.HTTP_STATUS_INTERNAL_SERVER_ERROR;
	}
	
	public DefaultWebApplicationException(Throwable e, int httpStatus) {
		super(e);
		this.httpStatus = httpStatus;
	}
	
	public DefaultWebApplicationException(Throwable e, int httpStatus, String message) {
		super(message, e);
		this.httpStatus = httpStatus;
	}
	
	public DefaultWebApplicationException(int httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public DefaultWebApplicationException(String message) {
		super(message);
		this.httpStatus = WebApplicationException.HTTP_STATUS_INTERNAL_SERVER_ERROR;
	}

	public int getHttpStatus() {
		return this.httpStatus;
	}
}
