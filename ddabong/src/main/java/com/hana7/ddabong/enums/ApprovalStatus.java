package com.hana7.ddabong.enums;

public enum ApprovalStatus {
	PENDING("승인 대기 중"),
	APPROVED("승인 완료"),
	REJECTED("승인 반려");

	private String description;

	ApprovalStatus(String description){
		this.description = description;
	}
}
