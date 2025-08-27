package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityReviewRequestDTO;
import com.hana7.ddabong.dto.ActivityReviewResponseDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.ActivityReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityReviewController {

    private final ActivityReviewService activityReviewService;

    @GetMapping("/myreview")
    public ResponseEntity<List<ActivityReviewResponseDTO>> getMyActivityReviews(Authentication authentication) {
        String email = authentication.getName();
        List<ActivityReviewResponseDTO> activityReviews = activityReviewService.getMyActivityReviews(email);
        return ResponseEntity.ok(activityReviews);
    }

    @PostMapping("/{activityPostId}/review")
    public ResponseEntity<Void> createActivityReview(@PathVariable Long activityPostId, @RequestBody ActivityReviewRequestDTO requestDTO, Authentication authentication) {
        String email = authentication.getName();
        activityReviewService.createActivityReview(activityPostId, email, requestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{activityPostId}/review")
    public ResponseEntity<List<ActivityReviewResponseDTO>> getActivityPostReviews(@PathVariable Long activityPostId) {
        List<ActivityReviewResponseDTO> activityReviews = activityReviewService.getActivityPostReviews(activityPostId);
        return ResponseEntity.ok(activityReviews);
    }

    @Tag(name = "활동 리뷰 삭제하기")
    @Operation(summary = "회원은 자신이 작성한 활동 리뷰를 삭제할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "활동 리뷰를 삭제했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                    description = "해당하는 회원 | 리뷰가 존재하지 않습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
            @ApiResponse(responseCode = "409",
                    description = "해당 작업을 수행할 권한이 없습니다. | 이미 삭제되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    @DeleteMapping("/myreview/{reviewId}")
    public ResponseEntity<?> deleteActivityReview(@PathVariable Long reviewId, Authentication authentication) {
        activityReviewService.deleteActivityReview(authentication.getName(), reviewId);
        return ResponseEntity.ok().build();
    }
}
