package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.gemini.GeminiRequest;
import com.hana7.ddabong.dto.gemini.GeminiResponse;
import com.hana7.ddabong.exception.GeminiApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

	@Value("${gemini.api.url}")
	private String apiUrl;
	@Value("${gemini.model}")
	private String model;
	@Value("${gemini.key}")
	private String apiKey;

	private final RestTemplate restTemplate;

	/** 프롬프트 한 개를 넣으면 요약 텍스트를 반환 */
	public String summarize(String prompt) {
		URI endpoint = UriComponentsBuilder.fromUriString(apiUrl)
				.path("/v1beta/models/{model}:generateContent")
				.queryParam("key", apiKey)
				.buildAndExpand(model)
				.toUri();

		GeminiRequest body = GeminiRequest.fromText(prompt);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GeminiRequest> req = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<GeminiResponse> res = restTemplate.exchange(
					endpoint, HttpMethod.POST, req, GeminiResponse.class
			);

			if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
				log.error("Gemini API 호출 실패: 응답 코드 {}", res.getStatusCode());
				throw new GeminiApiException("Gemini API 호출 실패: " + res.getStatusCode());
			}

			return res.getBody().getFirstCandidateText()
					.orElseThrow(() -> new GeminiApiException("Gemini 응답에서 요약 텍스트를 찾을 수 없습니다."));

		} catch (RestClientException e) {
			log.error("Gemini API 호출 중 오류 발생", e);
			throw new GeminiApiException("Gemini API 호출 중 오류: " + e.getMessage());
		}
	}
}

