package com.hana7.ddabong.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  protected HttpStatus httpStatus;
  protected String message;
  protected int errorCode;

  public BaseException(HttpStatus httpStatus, String message,  int errorCode) {
    this.httpStatus = httpStatus;
    this.message = message;
    this.errorCode = errorCode;
  }
}
