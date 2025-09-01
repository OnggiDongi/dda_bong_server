package com.hana7.ddabong.handler;

import com.hana7.ddabong.auth.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
public class OauthLoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Map<String, Object> body = JwtProvider.getClaims(authentication);

		String token = (String) body.get("accessToken");

		String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/success")
				.queryParam("accessToken", token)
				.build().toUriString();

		response.sendRedirect(redirectUrl);
	}
}
