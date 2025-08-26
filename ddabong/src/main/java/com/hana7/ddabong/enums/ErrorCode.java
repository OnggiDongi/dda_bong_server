package com.hana7.ddabong.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	// NOT_FOUND (1xx)
	NOT_FOUND_USER(101, "해당 아이디를 가진 회원을 찾을 수 없습니다."),
	NOT_FOUND_INSTITUTION(102, "해당 아이디를 가진 기관을 찾을 수 없습니다."),
	NOT_FOUND_REVIEW(103, "해당 리뷰를 찾을 수 없습니다."),
	NOT_FOUND_ACTIVITY_POST(104, "해당 활동 게시글을 찾을 수 없습니다."),


	// BAD_REQUEST (2xx)
	BAD_REQUEST_FUTURE_BIRTHDATE(201, "생년월일이 현재보다 미래일 수 없습니다."),
	BAD_REQUEST_INSTITUTION_MISMATCH(202, "리뷰를 작성하려는 활동의 주최 기관이 아닙니다."),
	BAD_REQUEST_NOT_APPLICANT(203, "해당 활동에 참여한 회원이 아닙니다."),
	BAD_REQUEST_NO_PERMISSION(204, "해당 작업을 수행할 권한이 없습니다."),


	// CONFLICT (3xx)
	CONFLICT_USER(301, "이미 존재하는 회원입니다."),
	CONFLICT_INSTITUTION(302, "이미 존재하는 기관입니다.");

	private final int errorCode;
	private final String errorMessage;

	ErrorCode(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
