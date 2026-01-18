package com.hana7.ddabong.service;

import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.dto.AccessTokenResponseDTO;
import com.hana7.ddabong.dto.MemberDTO;
import com.hana7.ddabong.dto.RefreshTokenRequestDTO;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.enums.ROLE;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.InstitutionRepository;
import com.hana7.ddabong.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RedisTemplate<String, String> redisTemplate;
	private final UserRepository userRepository;
	private final InstitutionRepository institutionRepository;

	public String getNewAccessToken(HttpServletRequest request) {

		String refreshToken = JwtProvider.extractTokenFromRequest(request);

		System.out.println("refreshToken = " + refreshToken);

		String email = JwtProvider.getEmailFromToken(refreshToken);

		User user = userRepository.findByEmail(email).orElse(null);
		Institution institution = institutionRepository.findByEmail(email).orElse(null);

		MemberDTO dto;
		if(user == null) {
			if(institution == null || institution.getDeletedAt() != null){
				throw new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION);
			}
			dto = new MemberDTO(institution.getId(),institution.getEmail(), institution.getPassword(), institution.getName(), ROLE.ROLE_INSTITUTION.name(), false);
		} else {
			dto = new MemberDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(),ROLE.ROLE_USER.name(), false);
		}

		Map<String, Object> claims1 = dto.getClaims();



		boolean isValid = isRefreshTokenValid(email, refreshToken);

		if(!isValid) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_REFRESHTOKEN_NOTVALID);
		}
		String refreshTokenFromRedis = getRefreshToken(email);

		if(refreshTokenFromRedis == null) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_REFRESHTOKEN_EXPIRED);
		}

		String accessToken = JwtProvider.generateToken(new HashMap<>(claims1), 60);
		System.out.println("accessToken = " + accessToken);

		return accessToken;
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
