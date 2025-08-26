package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityReviewRequestDTO;
import com.hana7.ddabong.dto.ActivityReviewResponseDTO;
import com.hana7.ddabong.service.ActivityReviewService;
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
}
