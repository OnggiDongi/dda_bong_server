package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.ActivityReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ActivityMyReviewResponseDTO {
	private Long id;
	private int rate;
	private String content;
	private String imageUrl;
	private Long activityId;
	private String activityTitle;
	private String createdAt;
	private String category;

	public static ActivityMyReviewResponseDTO toDTO(ActivityReview activityReview, String category) {
		return ActivityMyReviewResponseDTO.builder()
				.id(activityReview.getId())
				.rate(activityReview.getRate())
				.content(activityReview.getContent())
				.imageUrl(activityReview.getImageUrl())
				.activityId(activityReview.getActivity().getId())
				.activityTitle(activityReview.getActivity().getTitle())
				.createdAt(
						String.format("%d.%02d.%02d",
								activityReview.getCreatedAt().getYear(),
								activityReview.getCreatedAt().getMonthValue(),
								activityReview.getCreatedAt().getDayOfMonth())
				)
				.category(category)
				.build();
	}
}
