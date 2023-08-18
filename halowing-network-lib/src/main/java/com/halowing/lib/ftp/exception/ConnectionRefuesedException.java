package com.halowing.lib.ftp.exception;

public class ConnectionRefuesedException extends RuntimeException{

	private static final long serialVersionUID = 6908315287849489049L;
	
	public ConnectionRefuesedException(String hostname, Integer port) {
		super( String.format("ftp connection is refused. hostname = %s, port = %d.", hostname, port) );
	}

}
