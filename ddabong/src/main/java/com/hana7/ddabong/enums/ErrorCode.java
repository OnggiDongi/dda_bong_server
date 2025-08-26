package com.hana7.ddabong.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	NOTFOUND_USER(101, "해당 아이디를 가진 회원을 찾을 수 없습니다."),
	NOTFOUND_INSTITUTION(102, "해당 아이디를 가진 기관을 찾을 수 없습니다."),
	NOTFOUND_POST(103, "해당 아이디를 가진 봉사 모집글을 찾을 수 없습니다."),

	BAD_REQUEST_FUTURE_BIRTHDATE(201, "생년월일이 현재보다 미래일 수 없습니다."),

	CONFLICT_USER(301, "이미 존재하는 회원입니다."),
	CONFLICT_INSTITUTION(302, "이미 존재하는 기관입니다.");

	private final int errorCode;
	private final String errorMessage;

	ErrorCode(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
