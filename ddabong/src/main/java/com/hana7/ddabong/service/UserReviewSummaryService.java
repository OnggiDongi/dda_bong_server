package com.hana7.ddabong.service;

import com.hana7.ddabong.entity.UserReview;
import com.hana7.ddabong.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReviewSummaryService {

	private final UserReviewRepository userReviewRepository;
	private final GeminiService geminiService;

	@Value("${summary.chunk-size:10}")
	private int chunkSize;

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

		// 2) 청크 요약
		List<String> chunkSummaries = new ArrayList<>();
		for (int i = 0; i < lines.size(); i += chunkSize) {
			List<String> chunk = lines.subList(i, Math.min(i + chunkSize, lines.size()));
			String prompt = buildChunkPrompt(chunk);
			String chunkResult = geminiService.summarize(prompt);
			chunkSummaries.add(chunkResult);
		}

		// 3) 메타 요약
		String finalPrompt = buildMetaPrompt(chunkSummaries);
		return geminiService.summarize(finalPrompt);
	}

	private String buildChunkPrompt(List<String> chunk) {
		String joined = String.join("\n", chunk);
		return """
               너는 평가 요약가야. 아래의 리뷰 라인들을 중복 없이 간결하게 통합 요약해줘.

               요구:
               - 핵심 키워드 3~6개 (쉼표로)
               - 건강/성실/태도 경향을 자연어로
               - 칭찬/개선점 각 2~3개 (불릿)
               - 개선점 없으면 안 써도 됨
               - 전반 톤 한 줄

               리뷰:
               """ + joined;
	}

	private String buildMetaPrompt(List<String> chunkSummaries) {
		String joined = String.join("\n---\n", chunkSummaries);
		return """
               다음은 여러 청크의 1차 요약이야. 이를 다시 한번 간결하게 최종 요약해줘.

               요구:
               - 전체 경향/키워드/칭찬/개선/한줄톤 포함
               - 중복 제거 & 문장 다듬어서 간결하게

               1차 요약들:
               """ + joined;
	}

	private static String nullToDash(String s) {
		return (s == null || s.isBlank()) ? "-" : s;
	}
}
