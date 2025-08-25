package com.hana7.ddabong.dto;

import com.hana7.ddabong.enums.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOnboardingRequestDTO {
    @NotBlank(message = "봉사 선호 지역을 입력해주세요.")
    private String preferredRegion;
    @NotBlank(message = "관심 분야를 입력해주세요.")
    private List<Category> preferredCategory;
}
