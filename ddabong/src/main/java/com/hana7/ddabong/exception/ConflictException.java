package com.hana7.ddabong.exception;

import com.hana7.ddabong.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
	public ConflictException(ErrorCode errorCode) {
		super(HttpStatus.BAD_REQUEST, errorCode.getErrorMessage(), errorCode.getErrorCode());
	}
}
