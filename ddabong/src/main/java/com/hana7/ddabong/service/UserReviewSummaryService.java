package com.hana7.ddabong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.ddabong.entity.UserReview;
import com.hana7.ddabong.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReviewSummaryService {

	private final UserReviewRepository userReviewRepository;
	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;

	public String summarizeForUser(Long userId) {
		List<UserReview> reviews = userReviewRepository.findByUserId(userId);
		if (reviews.isEmpty()) {
			return "아직 등록된 사용자 리뷰가 없습니다.";
		}

		// 1) 리뷰 → 텍스트 라인화 (점수/메모 위주)
		List<String> lines = new ArrayList<>(reviews.size());
		for (UserReview r : reviews) {
			String line = String.format(
					"건강:%d, 성실:%d, 태도:%d, 메모:%s, 작성기관ID:%d",
					r.getHealthStatus(),
					r.getDiligenceLevel(),
					r.getAttitude(),
					nullToDash(r.getMemo()),
					r.getWriteInst()
			);
			lines.add(line);
		}

		// 2) 요약
		String prompt = buildPrompt(lines);
		return geminiService.summarize(prompt);
	}

	public Map<Long, String> summarizeForMultipleUsers(Map<Long, List<UserReview>> reviewsByUserId) {
		if (reviewsByUserId.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Long, List<String>> reviewLinesByUserId = reviewsByUserId.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream()
								.map(r -> String.format(
										"건강:%d, 성실:%d, 태도:%d, 메모:%s, 작성기관ID:%d",
										r.getHealthStatus(),
										r.getDiligenceLevel(),
										r.getAttitude(),
										nullToDash(r.getMemo()),
										r.getWriteInst()
								))
								.collect(Collectors.toList())
				));

		String prompt = buildBatchPrompt(reviewLinesByUserId);
		String jsonResponse = geminiService.summarize(prompt);

		try {
			// Gemini가 반환한 JSON 문자열에서 실제 JSON 부분만 추출
			String actualJson = jsonResponse.substring(jsonResponse.indexOf('{'), jsonResponse.lastIndexOf('}') + 1);
			return objectMapper.readValue(actualJson, new TypeReference<Map<Long, String>>() {});
		} catch (JsonProcessingException | StringIndexOutOfBoundsException e) {
			// JSON 파싱 실패 시, 각 사용자에 대해 개별적으로 요약을 다시 시도하거나 기본 메시지를 반환
			return reviewsByUserId.keySet().stream()
					.collect(Collectors.toMap(id -> id, id -> "요약 생성에 실패했습니다."));
		}
	}

	private String buildBatchPrompt(Map<Long, List<String>> reviewLinesByUserId) {
		String reviewsJson;
		try {
			reviewsJson = objectMapper.writeValueAsString(reviewLinesByUserId);
		} catch (JsonProcessingException e) {
			reviewsJson = "{}"; // 오류 발생 시 빈 JSON
		}

		return """
           너는 여러 사용자에 대한 평가 요약가야. 아래에 JSON 형식으로 사용자 ID와 리뷰 목록이 주어진다.
           각 사용자에 대한 리뷰를 바탕으로, 사용자별 요약을 생성해줘.

           요구사항:
           - 각 사용자에 대해, 태도와 건강상태를 두 문장 이내로 요약해줘.
           - 이 모든걸 50자 이내로 간결하게 작성해줘.
           - 따뜻한 말투로 작성해줘.
           - 글자를 강조하기 위한 ** 같은 마크다운 문법은 사용하지 마.
           - '봉사자들', '봉사자' 등의 단어는 사용하지 마.
           - 결과는 반드시 사용자 ID를 키로, 요약 내용을 값으로 하는 JSON 형식으로 반환해줘.
           - 요약내용에 사용자 ID 같이 말해줘.
           - 예시: {"101": "요약 내용1", "102": "요약 내용2"}

           리뷰 데이터:
           """ + reviewsJson;
	}



	private String buildPrompt(List<String> lines) {
		String joined = String.join("\n", lines);
		return """
           너는 봉사자 평가 요약가야. 아래의 리뷰 라인들을 중복 없이 간결하게 통합 요약해줘.

           요구:
           - 태도와 건강상태를 두 문장 이내 요약
           - 전체 50자 이내로 간결하게 작성
           - 따뜻한 말투로 작성
           - 글자를 강조하기 위한 ** 같은 마크다운 문법 사용 금지
           - '봉사자들', '봉사자' 등의 단어 사용 금지
           - 복수형 금지

           리뷰:
           """ + joined;
	}

	private static String nullToDash(String s) {
		return (s == null || s.isBlank()) ? "-" : s;
	}
}
