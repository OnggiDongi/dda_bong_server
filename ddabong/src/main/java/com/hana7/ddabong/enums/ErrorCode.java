package com.hana7.ddabong.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
	NOTFOUND_USER(101, "해당 아이디를 가진 회원을 찾을 수 없습니다."),
	NOTFOUND_INSTITUTION(102, "해당 아이디를 가진 기관을 찾을 수 없습니다."),
	NOTFOUND_POST(103, "해당 아이디를 가진 봉사 모집글을 찾을 수 없습니다."),
	NOTFOUND_REVIEW(104, "해당 리뷰를 찾을 수 없습니다."),
	NOTFOUND_ACTIVITY_POST(105, "해당 활동 게시글을 찾을 수 없습니다."),
	NOTFOUND_CERTIFICATION(106, "해당 인증서를 찾을 수 없습니다."),
	NOTFOUND_ACTIVITY(107, "해당 아이디를 가진 봉사 활동을 찾을 수 없습니다."),
	NOTFOUND_APPLICANT(108, "해당 회원은 해당 봉사를 신청하지 않았습니다."),

	BAD_REQUEST_FUTURE_BIRTHDATE(201, "생년월일이 현재보다 미래일 수 없습니다."),
	BAD_REQUEST_INSTITUTION_MISMATCH(202, "리뷰를 작성하려는 활동의 주최 기관이 아닙니다."),
	BAD_REQUEST_NOT_APPLICANT(203, "해당 활동에 참여한 회원이 아닙니다."),
	BAD_REQUEST_NO_PERMISSION(204, "해당 작업을 수행할 권한이 없습니다."),
	BAD_REQUEST_CERTIFICATION_ACCESS_DENIED(205, "해당 인증서를 조회할 권한이 없습니다."),
	BAD_REQUEST_ALREADY_APPLIED(206, "이미 해당 모집글에 봉사 신청이 완료되었습니다."),
	BAD_REQUEST_RECRUITMENT_DATE_EXPIRED(207, "모집 마감일이 지났습니다."),
	BAD_REQUEST_UNAUTHORIZED(208, "권한이 없습니다."),
	BAD_REQUEST_ACTIVITY_NOT_COMPLETED(209, "아직 완료되지 않은 활동입니다."),
	BAD_REQUEST_STATUS_NOT_PENDING(210, "봉사 신청상태가 대기중이 아닙니다."),
	BAD_REQUEST_ALREADY_DELETED(211, "이미 삭제되었습니다."),

	CONFLICT_USER(301, "이미 존재하는 회원입니다."),
	CONFLICT_INSTITUTION(302, "이미 존재하는 기관입니다."),
	CONFLICT_ACTIVITY_POST(303, "이미 존재하는 게시글 입니다");


	private final int errorCode;
	private final String errorMessage;

	ErrorCode(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
