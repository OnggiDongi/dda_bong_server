package com.hana7.ddabong.exception;

import com.hana7.ddabong.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class GeminiApiException extends BaseException {

    public GeminiApiException(ErrorCode errorCode, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, errorCode.getErrorCode());
        this.httpStatus =HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode.getErrorCode();
        this.message = message;
    }
}
