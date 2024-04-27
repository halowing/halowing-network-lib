package com.halowing.lib.web.exception;

import java.util.Locale;
import java.util.ResourceBundle;

import com.halowing.lib.exception.ApplicationException;

public class HttpNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = -4391515543765150039L;
	
	private static final String MESSAGE_RESOURCE = "messages/http-status-message";
	
	private static final ResourceBundle RESOURCE_BUNDLE_DEFAULT     = ResourceBundle.getBundle(MESSAGE_RESOURCE);
	private static final ResourceBundle LOCALE_RESOURCE_BUNDLE = ResourceBundle.getBundle(MESSAGE_RESOURCE, Locale.getDefault());

	public static final int HTTP_STATUS = 404;
	public static  final String ERROR_CODE = "http.status.not_found_error";
	private final String[] args;
	
	public HttpNotFoundException() {
		super(getMessage(null) );
		this.args = null;
	}
	
	public HttpNotFoundException(String arg) {
		super(getMessage(arg) );
		this.args = new String[1];
		args[0] =arg;
	}
	


	@Override
	public int getHttpStatus() {
		return HTTP_STATUS;
	}

	@Override
	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String[] getArgs() {
		return args;
	}

	@Override
	public String getLocalizedMessage(Locale locale) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, locale);
			return ApplicationException.getLocaleMessage(bundle, getCause(), ERROR_CODE, this.args);
		} catch (Exception e) {
			return super.getLocalizedMessage();
		}
	}
	
	@Override
	public String getLocalizedMessage() {
		
		try {
			return ApplicationException.getLocaleMessage(LOCALE_RESOURCE_BUNDLE, getCause(), ERROR_CODE, this.args);
		} catch (Exception e) {
			return super.getLocalizedMessage();
		}
	}
	
	private static String getMessage( String arg) {
		String[] args = new String[1];
		args[0] =arg;
		return ApplicationException.getLocaleMessage(RESOURCE_BUNDLE_DEFAULT, null, ERROR_CODE, args);
	}
}
