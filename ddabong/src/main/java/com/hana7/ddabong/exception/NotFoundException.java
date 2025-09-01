package com.hana7.ddabong.exception;

import com.hana7.ddabong.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
	public NotFoundException(ErrorCode errorCode) {
      super(HttpStatus.NOT_FOUND, errorCode.getErrorMessage(), errorCode.getErrorCode());
	  this.httpStatus =HttpStatus.NOT_FOUND;
	  this.errorCode = errorCode.getErrorCode();
	  this.message = errorCode.getErrorMessage();
	}
}
