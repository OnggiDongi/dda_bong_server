package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityReviewResponseDTO {

	private Long id;

	private String userName;

	private String profileImage;

	private int rate;

	private String comment;
}
