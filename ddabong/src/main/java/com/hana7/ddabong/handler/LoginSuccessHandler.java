package com.hana7.ddabong.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final RefreshTokenService refreshTokenService;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException {

		Map<String, Object> body = JwtProvider.getClaims(authentication);

		System.out.println(authentication.getPrincipal());
		refreshTokenService.saveRefreshToken(body.get("email").toString(), body.get("refreshToken").toString(), 60 * 24); // 1일

		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println(objectMapper.writeValueAsString(body));
		out.write(objectMapper.writeValueAsString(body));
		out.close();
	}
}
