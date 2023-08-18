package com.halowing.lib.web.exception;

public class HttpNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = -4391515543765150039L;

	public HttpNotFoundException() {
		super(404,"request url is not founded.");
	}
	
	public HttpNotFoundException(String message) {
		super(404, message);
		
	}
}
