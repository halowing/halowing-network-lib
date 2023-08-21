package com.halowing.lib.web.exception;

import java.util.Locale;
import java.util.ResourceBundle;

import com.halowing.lib.exception.ApplicationException;

public class SimpleWebApplicationException extends WebApplicationException {

	private static final long serialVersionUID = 7972287346429164189L;
	
	private static final String MESSAGE_RESOURCE = "messages/messages";
	
	private static final ResourceBundle RESOURCE_BUNDLE_US     = ResourceBundle.getBundle(MESSAGE_RESOURCE, Locale.US);
	private static final ResourceBundle LOCALE_RESOURCE_BUNDLE = ResourceBundle.getBundle(MESSAGE_RESOURCE, Locale.getDefault());
	
	private final int httpStatus;
	private final String errorCode;
	private final String[] args;
	
	public SimpleWebApplicationException() {
		super(getMessage(HTTP_STATUS_INTERNAL_SERVER_ERROR_CODE, null) );
		this.httpStatus = HTTP_STATUS_INTERNAL_SERVER_ERROR;
		this.errorCode = HTTP_STATUS_INTERNAL_SERVER_ERROR_CODE;
		this.args = null;
	}
	
	public SimpleWebApplicationException(Throwable e) {
		super(e);
		
		if(e instanceof ApplicationException) {
			ApplicationException e1 = (ApplicationException) e;
			this.httpStatus = HTTP_STATUS_INTERNAL_SERVER_ERROR;
			this.errorCode = e1.getErrorCode();
			this.args = e1.getArgs();
		}else if(e instanceof DefaultWebApplicationException) {
			DefaultWebApplicationException e1 = (DefaultWebApplicationException) e;
			this.httpStatus =e1.getHttpStatus();
			this.errorCode = null;
			this.args = null;
		}else{
			this.httpStatus = HTTP_STATUS_INTERNAL_SERVER_ERROR;
			this.errorCode = null;
			this.args = null;
		}
	}
	
	public SimpleWebApplicationException(int httpStatus, String errorCode, String... args) {
		super(getMessage(errorCode, args) );
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.args = args;;
	}
	
	public SimpleWebApplicationException(String errorCode, String... args) {
		this(HTTP_STATUS_INTERNAL_SERVER_ERROR, errorCode, args);
	}
	
	protected static String getMessage( String errorCode, String[] args) {
		return ApplicationException.getLocaleMessage(RESOURCE_BUNDLE_US, null, errorCode, args);
	}
	
	@Override
	public int getHttpStatus() {
		return this.httpStatus;
	}


	@Override
	public String getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String[] getArgs() {
		return this.args;
	}

	@Override
	public String getLocalizedMessage(Locale locale) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, locale);
			return ApplicationException.getLocaleMessage(bundle, getCause(), this.errorCode, this.args);
		} catch (Exception e) {
			return super.getLocalizedMessage();
		}
	}
	
	@Override
	public String getLocalizedMessage() {
		
		try {
			return ApplicationException.getLocaleMessage(LOCALE_RESOURCE_BUNDLE, getCause(), this.errorCode, this.args);
		} catch (Exception e) {
			return super.getLocalizedMessage();
		}
		
		
	}

}
