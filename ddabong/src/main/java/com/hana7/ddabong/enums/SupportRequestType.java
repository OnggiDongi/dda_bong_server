package com.hana7.ddabong.enums;

import lombok.Getter;

@Getter
public enum SupportRequestType {
	BUS("bus"),
	PLANCARD("plancard"),
	SNACK("snack");

	private String description;

	SupportRequestType(String description) {
		this.description = description;
	}

	public static SupportRequestType findByDescription(String description) {
		for (SupportRequestType supportRequestType : SupportRequestType.values()) {
			if (supportRequestType.getDescription().equals(description)) {
				return supportRequestType;
			}
		}
		return null;
	}
}
