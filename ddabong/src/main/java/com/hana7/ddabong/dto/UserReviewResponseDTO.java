package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.UserReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserReviewResponseDTO {
    private Long id;
    private int healthStatus;
    private int diligenceLevel;
    private int attitude;
    private String memo;
    private Long writeInst;
    private LocalDateTime createdAt;

    public static UserReviewResponseDTO toDTO(UserReview userReview) {
        return UserReviewResponseDTO.builder()
                .id(userReview.getId())
                .healthStatus(userReview.getHealthStatus())
                .diligenceLevel(userReview.getDiligenceLevel())
                .attitude(userReview.getAttitude())
                .memo(userReview.getMemo())
                .writeInst(userReview.getWriteInst())
                .createdAt(userReview.getCreatedAt())
                .build();
    }
}
