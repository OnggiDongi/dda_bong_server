package com.hana7.ddabong.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
public class KakaoUserInfo {
	private final String name;
	private final String email;
	private final String profileImage;
	private final String phoneNumber;
	private final LocalDate birthDate;

	public KakaoUserInfo(Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		name =  (String) account.get("name");
		email = (String) account.get("email");
		profileImage = (String) profile.get("thumbnail_image_url");
		phoneNumber = account.get("phone_number").toString().replace("+82 ", "0");
		birthDate = LocalDate.of(
				Integer.parseInt((String) account.get("birthyear")),
				Integer.parseInt(account.get("birthday").toString().substring(0, 2)),
				Integer.parseInt(account.get("birthday").toString().substring(2, 4))
		);
	}
}
