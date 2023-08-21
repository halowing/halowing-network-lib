package com.halowing.lib.web.exception;

import com.halowing.lib.exception.SimpleApplicationException;

public class WebApplicationException extends RuntimeException {

	private static final long serialVersionUID = 6630247127070485289L;
	
	private final int httpStatus;
	
	public WebApplicationException() {
		super("Internal Server error.");
		this.httpStatus = 500;
	}
	
	public WebApplicationException(SimpleApplicationException e) {
		super(e);
		this.httpStatus = 500;
	}
	
	public WebApplicationException(SimpleApplicationException e, int httpStatus) {
		super(e);
		this.httpStatus = httpStatus;
	}
	
	public WebApplicationException(SimpleApplicationException e, int httpStatus, String message) {
		super(message, e);
		this.httpStatus = httpStatus;
	}
	
	public WebApplicationException(int httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public WebApplicationException(String message) {
		super(message);
		this.httpStatus = 500;
	}

	public int getHttpStatus() {
		return this.httpStatus;
	}
}
