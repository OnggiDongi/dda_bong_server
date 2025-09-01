package com.hana7.ddabong.exception;

import com.hana7.ddabong.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
	public BadRequestException(ErrorCode errorCode) {
		super(HttpStatus.BAD_REQUEST, errorCode.getErrorMessage(), errorCode.getErrorCode());
		this.httpStatus =HttpStatus.BAD_REQUEST;
		this.errorCode = errorCode.getErrorCode();
		this.message = errorCode.getErrorMessage();
	}
}
