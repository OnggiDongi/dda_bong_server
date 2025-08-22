package com.hana7.ddabong.enums;

import lombok.Getter;

@Getter
public enum Category {
	LIVING("생활 · 주거 "),
	EDUCATION("교육 · 상담 · 멘토링"),
	SAFETY("보건 · 안전 · 재난"),
	CULTURE("문화 · 예술"),
	ENVIRONMENT("환경"),
	PUBLIC("행정 · 공익"),
	GLOBAL("농어촌 · 국제협력");

	private String description;

	Category(String description){
		this.description = description;
	}
}
