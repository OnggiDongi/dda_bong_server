package com.hana7.ddabong.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.ddabong.dto.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final String[] excludePatterns = {
			"/login",
			"/users/signin",
			"/users/signup",
			"/institutions/signup",
			"/api/public/**",
			"/favicon.ico",
			"/actuator/**",
			"/*.html",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/upload/**",
			"/kakao/login",
			"/auth/refresh"
	};

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request){
		String path = request.getRequestURI();
		System.out.println(path);
		return Arrays.stream(excludePatterns)
				.anyMatch(pattern -> pathMatcher.match(pattern, path));
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();

		// ✅ refresh 요청은 accessToken 없이 통과시켜야 함
		if (uri.equals("/auth/refresh")) {
			filterChain.doFilter(request, response);
			return;
		}
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		System.out.println("authHeader: " + authHeader);
		try {
			System.out.println();
			System.out.println("** JwtAuthenticationFilter.doFilterInternal:" + authHeader.substring(7));
			Map<String, Object> claims = JwtProvider.validateToken(authHeader.substring(7));

			String email = (String)claims.get("email");
			String name = (String)claims.get("name");
			String role = (String)claims.get("role");
			MemberDTO dto = new MemberDTO(email, "", name, role);
			UsernamePasswordAuthenticationToken authenticationToken = new
					UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities());

			// 올바른 Authorization을 저장하여 어디서든 불러올 수 있다!
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			PrintWriter out = response.getWriter();
			out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN")));
			out.close();
		}
		filterChain.doFilter(request, response);
	}
}


