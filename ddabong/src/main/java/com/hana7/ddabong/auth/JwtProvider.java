package com.hana7.ddabong.auth;

import com.hana7.ddabong.dto.MemberDTO;
import com.hana7.ddabong.dto.OauthUserDTO;
import com.hana7.ddabong.enums.ROLE;
import com.hana7.ddabong.exception.CustomJwtException;
import com.hana7.ddabong.service.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

	// TODO : application.yml로 숨기기
	@Value("${jwt.secret}")
	private static String secret = "eowRQesdOayD97KkmQVbUCkBZUWZvqf8";

	private static final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

	// Authentication 속 pricipal 정보들로 JWT Token 생성하기
	public static String generateToken(Map<String, Object> valueMap, int min) {
		return Jwts.builder().setHeader(Map.of("typ", "JWT"))
				.setClaims(valueMap)
				.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
				.signWith(key).compact();
	}

	public static Map<String, Object> getClaims(Authentication authentication) {
		MemberDTO dto;
		Map<String, Object> claims =  new HashMap<>();

		if (authentication.getPrincipal() instanceof MemberDTO d) { // 일반 로그인
			dto = new MemberDTO(d.getEmail(), "", d.getName(), d.getRole());
		} else { // 카카오 로그인
			DefaultOAuth2User d = (DefaultOAuth2User) authentication.getPrincipal();

			Map<String, Object> properties = (Map<String, Object>) d.getAttributes().get("properties");
			String name = properties.get("nickname").toString();

			Map<String, Object> kakao_account = (Map<String, Object>) d.getAttributes().get("kakao_account");
			String email = kakao_account.get("email").toString();

			dto = new MemberDTO(email, "", name, ROLE.ROLE_USER.name());
		}
		claims = dto.getClaims();


		String accessToken  = generateToken(new HashMap<>(claims), 60);
		String refreshToken = generateToken(new HashMap<>(claims), 60 * 24);

		Map<String, Object> body = new HashMap<>(claims);
		body.put("accessToken", accessToken);
		body.put("refreshToken", refreshToken);

		return body;
	}

	public static Map<String, Object> validateToken(String token) {
		Map<String, Object> claim = null;

		try {
			claim = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token).getBody();
		} catch (WeakKeyException e) {
			throw new CustomJwtException("WeakException");
		} catch (MalformedJwtException e) {
			throw new CustomJwtException("MalFormed");
		} catch (ExpiredJwtException e) {
			throw new CustomJwtException("Expired");
		} catch (InvalidClaimException e) {
			throw new CustomJwtException("Invalid");
		} catch (JwtException e) {
			throw new CustomJwtException("JwtError");
		} catch (Exception e) {
			throw new CustomJwtException("UnknownError");
		}

		return claim;
	}

	public static String getEmailFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		return (String) claims.get("email");
	}
}
