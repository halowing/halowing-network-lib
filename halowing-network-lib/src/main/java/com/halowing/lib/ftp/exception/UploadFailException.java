package com.halowing.lib.ftp.exception;

import java.nio.file.Path;
import java.util.Set;

public class UploadFailException extends RuntimeException {

	private static final long serialVersionUID = 5537579089328126596L;

	public UploadFailException(Set<Path> error) {
		super(collectMessage(error));
	}

	public UploadFailException(String hostname, Integer port, Path source, Path target) {
		super(String.format("file upload is failed. hostname=%s, port=%d, source file = %s, target file = %s", hostname, port, 
				source.toAbsolutePath().normalize().toString(), 
				target.toAbsolutePath().normalize().toString()
				)
			);
	}

	private static String collectMessage(Set<Path> error) {
		
		if(error == null || error.isEmpty())
			return "error file is unknown.";
		
		StringBuffer sb = new StringBuffer();
		
		error.stream()
		.forEach(it -> sb.append(it.getFileName()).append(","));
	
		return sb.toString();
	}
}
