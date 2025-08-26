package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserReviewRequestDTO;
import com.hana7.ddabong.dto.UserReviewResponseDTO;
import com.hana7.ddabong.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/reviews")
public class UserReviewController {

    private final UserReviewService userReviewService;

    @PostMapping
    public ResponseEntity<Void> createUserReview(@PathVariable Long userId, @RequestBody UserReviewRequestDTO requestDTO) {
        userReviewService.createUserReview(userId, requestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserReviewResponseDTO>> getUserReviews(@PathVariable Long userId) {
        List<UserReviewResponseDTO> userReviews = userReviewService.getUserReviews(userId);
        return ResponseEntity.ok(userReviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteUserReview(@PathVariable Long userId, @PathVariable Long reviewId) {
        userReviewService.deleteUserReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
