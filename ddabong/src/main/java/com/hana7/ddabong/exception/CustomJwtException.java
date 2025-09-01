package com.hana7.ddabong.exception;

import com.hana7.ddabong.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class CustomJwtException extends RuntimeException {
	private final HttpStatus httpStatus;
	protected String message;

	public CustomJwtException(String msg) {
		super("JwtErr:" + msg);
		this.httpStatus = HttpStatus.BAD_REQUEST;
		this.message = msg;
	}
}
