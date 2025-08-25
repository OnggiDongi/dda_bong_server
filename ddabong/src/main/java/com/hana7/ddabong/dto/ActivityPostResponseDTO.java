package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPostResponseDTO {
    private Long id;
    private String title;
    private LocalDateTime endAt;
    private String location;
    private String imageUrl;
    private Category category;

    public static ActivityPostResponseDTO of(ActivityPost activityPost) {
        return ActivityPostResponseDTO.builder()
                .id(activityPost.getId())
                .title(activityPost.getTitle())
                .endAt(activityPost.getEndAt())
                .location(activityPost.getLocation())
                .imageUrl(activityPost.getImageUrl())
                .category(activityPost.getActivity().getCategory())
                .build();
    }
}
