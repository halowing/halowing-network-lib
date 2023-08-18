package com.halowing.lib.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.halowing.lib.ftp.exception.ConnectionRefuesedException;
import com.halowing.lib.ftp.exception.LoginFailureException;
import com.halowing.lib.ftp.exception.UploadFailException;
import com.halowing.lib.validate.ParameterCheck;

public class SimpleFTPClient {
	
	private static final int RETRY_LIMIT =3;
	
	private static final Logger log = LoggerFactory.getLogger(SimpleFTPClient.class);
	
	private final static Integer DEFAULT_FTP_PORT = 21;
	
	private final FTPClient ftpClient = new FTPClient();;
	
	private final String hostname;
	
	private final Integer port;
	
	private final boolean passiveMode;
	
	private final String username;
	
	private final String password;
	
	public SimpleFTPClient(FTPConnectionProperty ftpConnectionProperty) {
		
		this.hostname = ftpConnectionProperty.getHostname();
		ParameterCheck.isNotBlink(hostname, "hostname"); 
		
		Integer serverPort = ftpConnectionProperty.getPort();
		if( serverPort == null)
			serverPort = DEFAULT_FTP_PORT ;
		
		this.port = serverPort;
		
		Boolean mode = ftpConnectionProperty.getPassiveMode();
		if(mode == null) 
			mode = true;
		
		this.passiveMode = mode;
		
		this.username = ftpConnectionProperty.getUsername();
		this.password = ftpConnectionProperty.getPassword();
		
	}

	private void initFtpClient() {
		
		if(passiveMode)
			ftpClient.enterLocalPassiveMode();
		else 
			ftpClient.enterLocalActiveMode();
		
//		file type을 binary type으로 설정 
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 한개의 파일을 upload 한다.
	 * 
	 * @param source
	 * @param target
	 * @throws Exception 
	 */
	public void upload(Path source, Path target) throws Exception {
		
		connect();
		
		login();
		
		try {
			upload1(source, target);
		} catch (Exception e) {
			throw e;
		} finally {
			ftpClient.logout();
			disconnect();
		}
	}
	
	public void upload(Collection<Path> sources, Path target) throws Exception {
		
		connect();
		
		login();
		
		Set<Path> error = new HashSet<>();
		
		try {
			
			sources.parallelStream()
			.forEach(source -> {
				try {
					upload1(source, target);
				} catch (Exception e) {
					log.error(e.getLocalizedMessage());
					error.add(source);
				} 
			}); 
			
			if(error.size() > 0) 
				throw new UploadFailException(error);
			
		} catch (Exception e) {
			throw e;
		} finally {
			ftpClient.logout();
			
			disconnect();
		}
	}
	
	private void login() {
		login(0);
		initFtpClient();
	}
	private void login(int retryCount) {
		if(retryCount > RETRY_LIMIT)
		{
			log.error("ftp login is not success. retry is over {} times.", RETRY_LIMIT);
			throw new LoginFailureException(hostname, port, username);
			
		}else if( retryCount > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		if( hostname != null && hostname.trim().length() > 0) {
			try {
				ftpClient.login(username, password);
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
				login(++retryCount);
			}
		}
	}
	
	private void upload1(Path source, Path target) {
		upload1(source, target, 0);
	}
	private void upload1(Path source, Path target, int retryCount) {
		if(retryCount > RETRY_LIMIT)
		{
			log.error("file upload is failed. retry is over {} times.", RETRY_LIMIT);
			throw new UploadFailException(hostname, port, source, target);
			
		}else if( retryCount > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
			}
		}else {
			source = source.toAbsolutePath().normalize();
			
			System.out.println("source = "+source.toString());
			
			target = target.normalize();
			
			System.out.println("target = "+target.toString());
		}
		
		
		Path parent = target.getParent();
		
		changeDirectory(parent);
		
		
		try (InputStream is = Files.newInputStream(source, StandardOpenOption.READ);)
		{
			String targetFileName = target.getFileName().toString();
			log.debug("[FTP] TARGET FILE NAME = {}", targetFileName);
			System.out.println("[FTP] TARGET FILE NAME = "+ targetFileName);
			
			ftpClient.storeFile(targetFileName, is);
			is.close();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			upload1(source, target, ++retryCount);
		} 
	}
	
	
	private void changeDirectory(Path path) {
		
		path.forEach(it -> {
			String dir = it.getFileName().toString();
			
			System.out.println("directory = "+ dir);
			
			changeDirectory(dir, 0);
		});
		
	}
	private void changeDirectory(String path, int retryCount) {
		if(retryCount > RETRY_LIMIT)
		{
			log.error("ftp login is not success. retry is over {} times.", RETRY_LIMIT);
			throw new ConnectionRefuesedException(hostname, port);
			
		}else if( retryCount > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		try {
			boolean s = ftpClient.changeWorkingDirectory(path);
			if(!s) {
				ftpClient.makeDirectory(path);
				changeDirectory(path, retryCount);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			changeDirectory(path, retryCount + 1);
		}
		
	}
	
	public <T> Optional<T> download(Path source, Path target) {
		
		T rs = null;
		
		return Optional.of(rs);
	}
	
	private void connect()  {
		connect(0);
	}
	private void connect(int retryCount)  {
		
		if(retryCount > RETRY_LIMIT)
		{
			log.error("ftp connection is not success. retry is over {} times.", RETRY_LIMIT);
			throw new ConnectionRefuesedException(hostname, port);
			
		}else if( retryCount > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ftpClient.connect(hostname, port);
		} catch (IOException e) {
			e.printStackTrace();
			connect(++retryCount);
		}
		
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.error("FTP server refused connection. hostname = {}, port = {}", hostname, port);
			disconnect();
		}
		
		
	}
	
	private void disconnect() {
		disconnect(0);
	}
	private void disconnect(int retryCount) {
		if(retryCount > RETRY_LIMIT)
		{
			log.error("ftp disconnect is not success. retry is over {} times.", RETRY_LIMIT);
			throw new ConnectionRefuesedException(hostname, port);
		}else if( retryCount > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				disconnect(++retryCount);
			}
		}
	}
	
	
	public static class FTPConnectionProperty{
		private String 	hostname;
		private Integer port;
		private String 	username;
		private String 	password;
		
		private Boolean passiveMode;

		public String getHostname() {
			return hostname;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Boolean getPassiveMode() {
			return passiveMode;
		}

		public void setPassiveMode(Boolean passiveMode) {
			this.passiveMode = passiveMode;
		}
		
		
	}
	
	
}
