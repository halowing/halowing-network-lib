package com.halowing.lib.ftp.exception;


public class LoginFailureException extends RuntimeException{

	private static final long serialVersionUID = 6908315287849489049L;
	
	public LoginFailureException(String hostname, Integer port, String username) {
		super( String.format("ftp login is failed. hostname = %s, port = %d, username = %s.", hostname, port,username) );
	}

}
