package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPostResponseDTO {
    private Long id;
    private String title;
    private String endAt;
    private String location;
    private String imageUrl;
    private Category category;
    private String dDay;
    private int applicantNum = 0;

    public static ActivityPostResponseDTO of(ActivityPost activityPost) {
        return ActivityPostResponseDTO.builder()
                .id(activityPost.getId())
                .title(activityPost.getTitle())
                .endAt(String.format(
                        "%d.%02d.%02d",
                        activityPost.getEndAt().getYear(),
                        activityPost.getEndAt().getMonthValue(),
                        activityPost.getEndAt().getDayOfMonth())
                )
                .location(activityPost.getLocation())
                .imageUrl(activityPost.getImageUrl())
                .category(activityPost.getActivity().getCategory())
                .build();
    }
    public static ActivityPostResponseDTO fromEntity(ActivityPost activityPost) {
        LocalDate now = LocalDate.now();
        long dDay = ChronoUnit.DAYS.between(now, activityPost.getRecruitmentEnd());
        String strDday;

        if (dDay > 0) {
            strDday = String.format("D-%d", dDay);
        } else if (dDay < 0) {
            strDday = "모집 마감";
        } else {
            strDday = "D-DAY";
        }

        return ActivityPostResponseDTO.builder()
                .id(activityPost.getId())
                .title(activityPost.getTitle())
                .endAt(String.format(
                        "%d.%02d.%02d",
                        activityPost.getEndAt().getYear(),
                        activityPost.getEndAt().getMonthValue(),
                        activityPost.getEndAt().getDayOfMonth())
                )
                .location(activityPost.getLocation())
                .imageUrl(activityPost.getImageUrl())
                .category(activityPost.getActivity().getCategory())
                .dDay(strDday)
                .build();
    }


}
