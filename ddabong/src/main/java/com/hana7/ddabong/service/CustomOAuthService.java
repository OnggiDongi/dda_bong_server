package com.hana7.ddabong.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.ddabong.dto.KakaoUserInfo;
import com.hana7.ddabong.dto.MemberDTO;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		// OAuth2 로그인 진행 시 키가 되는 필드값. kakao -> id
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();

		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println("attributes = " + attributes);
		OAuth2AccessToken accessToken = userRequest.getAccessToken();

		KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

		User findUser = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElse(null);

		if(findUser == null) {
			User newUser = User.builder()
					.name(kakaoUserInfo.getName())
					.email(kakaoUserInfo.getEmail())
					.password(attributes.get("id").toString())
					.isKakao(true)
					// TODO : 카카오 심사 끝나면 원래 정보들로 채워놓기 -> KakaoUserInfo 수정 후에 하기
					.phoneNumber("010-0000-0000")
					.birthdate(LocalDate.now())
					.build();
			userRepository.save(newUser);
		}

		// Security context에 저장할 객체 생성
		return new DefaultOAuth2User(new ArrayList<>(), attributes, userNameAttributeName);
	}

}
