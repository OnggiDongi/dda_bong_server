package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityRequestDTO {

    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수 값입니다.")
    private Category category;

    public Activity toEntity(Long institutionId) {
        return Activity.builder()
                .title(title)
                .content(content)
                .category(category)
                .institution(Institution.builder().id(institutionId).build())
                .build();
    }
}
