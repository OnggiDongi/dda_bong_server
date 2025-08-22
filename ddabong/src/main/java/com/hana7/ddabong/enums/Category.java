package com.hana7.ddabong.enums;

import lombok.Getter;

@Getter
public enum Category {
	LIVING("생활"),
	EDUCATION("교육"),
	SAFETY("보건"),
	CULTURE("문화"),
	ENVIRONMENT("환경"),
	PUBLIC("행정"),
	GLOBAL("농어촌");

	private String description;

	Category(String description){
		this.description = description;
	}
}
