package com.hana7.ddabong.exception;

public class CustomJwtException extends RuntimeException {
	public CustomJwtException(String msg) {
		super("JwtErr:" + msg);
	}
}
