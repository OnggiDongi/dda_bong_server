package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.ActivityPost;
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
    private String content;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String location;
    private String imageUrl;

    public static ActivityPostResponseDTO of(ActivityPost activityPost) {
        return ActivityPostResponseDTO.builder()
                .id(activityPost.getId())
                .title(activityPost.getTitle())
                .content(activityPost.getContent())
                .startAt(activityPost.getStartAt())
                .endAt(activityPost.getEndAt())
                .location(activityPost.getLocation())
                .imageUrl(activityPost.getImageUrl())
                .build();
    }
}
