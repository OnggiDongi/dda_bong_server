package com.hana7.ddabong.enums;

import com.hana7.ddabong.exception.NotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
	LIVING("생활"),
	EDUCATION("교육"),
	SAFETY("보건"),
	CULTURE("문화"),
	ENVIRONMENT("환경"),
	PUBLIC("행정"),
	RURALAREA("농어촌");

	private String description;

	Category(String description){
		this.description = description;
	}

	public static Category fromDescription(String desc) {
		return Arrays.stream(Category.values())
				.filter(c -> c.getDescription().equals(desc))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_CATEGORY));
	}
}
