package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenResponseDTO {
	private String accessToken;
}
