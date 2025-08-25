package com.hana7.ddabong.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String message;
  private final int errorCode;

  public BaseException(HttpStatus httpStatus, String message,  int errorCode) {
    this.httpStatus = httpStatus;
    this.message = message;
    this.errorCode = errorCode;
  }
}
