package com.hana7.ddabong.dto;

import lombok.Getter;

@Getter
public class UserReviewRequestDTO {
    private Long activityPostId;
    private int healthStatus;
    private int diligenceLevel;
    private int attitude;
    private String memo;
}
