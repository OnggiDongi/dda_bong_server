package com.hana7.ddabong.controller;

import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.dto.AccessTokenResponseDTO;
import com.hana7.ddabong.dto.RefreshTokenRequestDTO;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.RefreshTokenService;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		System.out.println("request = " + request.getHeader("Authorization"));
		String newAccessToken = refreshTokenService.getNewAccessToken(request);

		ResponseEntity<Object> build = ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
				.build();
		System.out.println("build = " + build);
		return build;
	}
}
