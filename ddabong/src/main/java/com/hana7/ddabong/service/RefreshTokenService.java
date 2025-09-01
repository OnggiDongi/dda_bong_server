package com.hana7.ddabong.service;

import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.dto.AccessTokenResponseDTO;
import com.hana7.ddabong.dto.RefreshTokenRequestDTO;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RedisTemplate<String, String> redisTemplate;

	public AccessTokenResponseDTO getNewAccessToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
		String refreshToken = refreshTokenRequestDTO.getRefreshToken();

		String email = JwtProvider.getEmailFromToken(refreshToken);
		boolean isValid = isRefreshTokenValid(email, refreshTokenRequestDTO.getRefreshToken());

		if(!isValid) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_REFRESHTOKEN_NOTVALID);
		}
		String refreshTokenFromRedis = getRefreshToken(email);

		if(refreshTokenFromRedis == null) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_REFRESHTOKEN_EXPIRED);
		}

		Map<String, Object> claims =  new HashMap<>();
		claims.put("email", email);
		claims.put("refreshToken", refreshTokenRequestDTO.getRefreshToken());
		String accessToken = JwtProvider.generateToken(claims, 60);

		return AccessTokenResponseDTO.builder()
				.accessToken(accessToken)
				.build();
	}

	// 저장
	public void saveRefreshToken(String email, String refreshToken, long expireMinutes) {
		redisTemplate.opsForValue().set(
				email,
				refreshToken,
				Duration.ofMinutes(expireMinutes)
		);
	}

	// 조회
	public String getRefreshToken(String email) {
		return redisTemplate.opsForValue().get(email);
	}

	// 삭제
	public void deleteRefreshToken(String email) {
		redisTemplate.delete(email);
	}

	// 검증
	public boolean isRefreshTokenValid(String email, String refreshToken) {
		String stored = redisTemplate.opsForValue().get(email);
		return refreshToken.equals(stored);
	}
}
