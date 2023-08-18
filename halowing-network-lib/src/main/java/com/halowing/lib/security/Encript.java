package com.halowing.lib.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.net.util.Base64;

import com.halowing.lib.exception.SimpleRuntimeException;


public class Encript {

	private static final String ENC_TYPE = "SHA-256";

	public static String encriptSHA256(String password, String salt) throws NoSuchAlgorithmException {
		if(password == null ) return null;
		
		if(salt == null) {
			throw new SimpleRuntimeException("salt can't be null1.");
		}
		
		MessageDigest md = MessageDigest.getInstance(ENC_TYPE);
		md.reset();
		md.update(salt.getBytes());
		byte[] hash = md.digest(password.getBytes());
		
		return new String(Base64.encodeBase64(hash, true));
	}
}
