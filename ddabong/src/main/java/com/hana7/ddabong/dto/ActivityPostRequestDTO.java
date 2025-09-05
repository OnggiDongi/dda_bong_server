package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.ActivityPost;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ActivityPostRequestDTO {

    @NotBlank(message = "제목은 필수 값입니다.")
    @Size(max = 20, message = "제목은 최대 20자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotNull(message = "봉사활동ID은 필수 값입니다.")
    private Long activityId;

    @NotBlank(message = "활동 시작일시는 필수 값입니다. (yyyy.MM.dd HH:mm)")
    private String startAt;

    @NotNull(message = "총 활동 시간은 필수 값입니다. (HH)")
    private int activityTime;

    @NotBlank(message = "모집 종료일시는 필수 값입니다. (yyyy.MM.dd)")
    private String recruitmentEnd;

    @NotBlank(message = "활동 장소는 필수 값입니다.")
    @Size(max = 128, message = "장소는 최대 128자까지 가능합니다.")
    private String location;

    @NotNull
    private int capacity;

    private MultipartFile image;

    private List<String> supports;

    public ActivityPost toEntity(
            String fileUrl,
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDateTime recruitmentEnd,
            Activity activity
    ) {
        return ActivityPost.builder()
                .title(title)
                .content(content)
                .startAt(startAt)
                .endAt(endAt)
                .recruitmentStart(LocalDateTime.now())
                .recruitmentEnd(recruitmentEnd)
                .capacity(capacity)
                .location(location)
                .imageUrl(fileUrl)
                .activity(activity)
                .build();
    }
}
