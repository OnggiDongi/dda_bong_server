package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.ActivityReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ActivityReviewResponseDTO {
    private Long id;
    private int rate;
    private String content;
    private String imageUrl;
    private Long activityId;
    private String activityTitle;
    private LocalDateTime createdAt;

    public static ActivityReviewResponseDTO toDTO(ActivityReview activityReview) {
        return ActivityReviewResponseDTO.builder()
                .id(activityReview.getId())
                .rate(activityReview.getRate())
                .content(activityReview.getContent())
                .imageUrl(activityReview.getImageUrl())
                .activityId(activityReview.getActivity().getId())
                .activityTitle(activityReview.getActivity().getTitle())
                .createdAt(activityReview.getCreatedAt())
                .build();
    }
}
