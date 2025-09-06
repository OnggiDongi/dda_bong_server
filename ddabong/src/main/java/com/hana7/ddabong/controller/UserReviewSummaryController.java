package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserReviewSummaryResponseDTO;
import com.hana7.ddabong.service.UserReviewSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI 리뷰 요약", description = "리뷰 요약 API (Gemini)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/summary")
public class UserReviewSummaryController {

	private final UserReviewSummaryService userReviewSummaryService;

	@Operation(
		summary = "사용자 리뷰 요약 얻기",
		description = """
            주어진 userId의 전체 UserReview들을 모아서 청크 요약 → 메타 요약 후 최종 요약을 반환합니다.
            - 리뷰가 없으면 안내 메시지를 반환합니다.
            - 내부적으로 GeminiService.summarize()를 사용합니다.
            """
	)
	@GetMapping("/{userId}")
	// 필요 시 권한 체크 (기관만 접근 등) — Security 쓰면 아래 주석 해제
	// @PreAuthorize("hasRole('INSTITUTION')")
	public ResponseEntity<UserReviewSummaryResponseDTO> getSummary(
		@Parameter(description = "요약할 대상 사용자 ID", example = "1")
		@PathVariable Long userId
	) {
		String summary = userReviewSummaryService.summarizeForUser(userId);
		return ResponseEntity.ok(new UserReviewSummaryResponseDTO(userId, summary));
	}
}
