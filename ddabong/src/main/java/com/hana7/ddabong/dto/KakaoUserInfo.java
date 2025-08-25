package com.hana7.ddabong.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo {
	private final String name;
	private final String email;
	private final String profile_image_url;

	public KakaoUserInfo(Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");
		name =  (String) profile.get("nickname");
		email = (String) account.get("email");
		profile_image_url = (String) profile.get("thumbnail_image_url");
	}
}
