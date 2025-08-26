package com.hana7.ddabong.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ActivityReviewRequestDTO {

    @NotBlank(message = "평점을 입력해주세요.")
    @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
    private int rate;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String imageUrl;
}
