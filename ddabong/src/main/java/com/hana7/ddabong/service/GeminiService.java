package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.gemini.GeminiRequest;
import com.hana7.ddabong.dto.gemini.GeminiResponse;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.GeminiApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class GeminiService {

	@Value("${gemini.api.url}")
	private String apiUrl;
	@Value("${gemini.model}")
	private String model;
	@Value("${gemini.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();

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

				throw new GeminiApiException(ErrorCode.SERVER_GEMINI_FAIL_CALL,"Gemini API 호출 실패: " + res.getStatusCode());
			}

			return res.getBody().getFirstCandidateText()
					.orElseThrow(() -> new GeminiApiException(ErrorCode.SERVER_GEMINI_NOTFOUND_TEXT,"Gemini 응답에서 요약 텍스트를 찾을 수 없습니다."));

		} catch (RestClientException e) {
			System.out.println("Gemini API 호출 중 오류: " + e.getMessage());
			throw new GeminiApiException(ErrorCode.SERVER_GEMINI_FAIL_CALLING,"Gemini API 호출 중 오류: " + e.getMessage());
		}
	}
}

