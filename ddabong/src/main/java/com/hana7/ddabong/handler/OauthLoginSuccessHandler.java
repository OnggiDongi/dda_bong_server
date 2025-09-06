package com.hana7.ddabong.handler;

import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OauthLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final RefreshTokenService refreshTokenService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Map<String, Object> body = JwtProvider.getClaims(authentication);

		String token = (String) body.get("accessToken");
		refreshTokenService.saveRefreshToken(body.get("email").toString(), body.get("refreshToken").toString(), 60 * 24); // 1일

		String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/success")
				.queryParam("accessToken", token)
				.build().toUriString();

		response.sendRedirect(redirectUrl);
	}
}
