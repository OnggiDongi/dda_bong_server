package com.hana7.ddabong.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
	private String email;
	private String refreshToken;
}
