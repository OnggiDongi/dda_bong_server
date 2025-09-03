package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApplicantDetailResponseDTO {
	private String userName;
	private String birthDate;
	private String phoneNumber;
	private String profileImage;
	private String preferredCategory;

	@Builder.Default
	private String reviewSummary = "";

	private double totalGrade;
	private double healthStatus;
	private double diligenceLevel;
	private double attitude;

	private List<UserReviewResponseDTO> userReviews;
}
