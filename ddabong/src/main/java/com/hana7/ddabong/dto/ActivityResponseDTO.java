package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ActivityResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Category category;

    public static ActivityResponseDTO fromEntity(Activity activity) {
        return ActivityResponseDTO.builder()
                .id(activity.getId())
                .content(activity.getContent())
                .title(activity.getTitle())
                .category(activity.getCategory())
                .build();
    }
}
