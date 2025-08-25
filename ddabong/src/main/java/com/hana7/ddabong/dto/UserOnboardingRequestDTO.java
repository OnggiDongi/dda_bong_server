package com.hana7.ddabong.dto;

import com.hana7.ddabong.enums.Category;
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
    private String preferredRegion;
    private List<Category> preferredCategory;
}
