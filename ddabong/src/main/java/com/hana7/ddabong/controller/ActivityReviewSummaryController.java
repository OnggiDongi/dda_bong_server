package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityReviewSummaryResponseDTO;
import com.hana7.ddabong.service.ActivityReviewSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI 리뷰 요약", description = "리뷰 요약 API (Gemini)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/summary")
public class ActivityReviewSummaryController {

    private final ActivityReviewSummaryService activityReviewSummaryService;

    @Operation(
            summary = "봉사 리뷰 요약 얻기",
            description = """
            주어진 activityId의 전체 봉사 리뷰들을 모아서 요약 후 최종 요약을 반환합니다.
            - 리뷰가 없으면 안내 메시지를 반환합니다.
            - 내부적으로 GeminiService.summarize()를 사용합니다.
            """
    )
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityReviewSummaryResponseDTO> getSummary(
            @Parameter(description = "요약할 대상 봉사 ID", example = "1")
            @PathVariable Long activityId
    ) {
        String summary = activityReviewSummaryService.summarizeForActivity(activityId);
        return ResponseEntity.ok(new ActivityReviewSummaryResponseDTO(activityId, summary));
    }
}
