package com.hana7.ddabong.dto;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantReviewResponseDTO {
	private Long id;

	private String name;

	private String profileImage;

	private double rate;

	private double healthStatus;

	private double diligenceLevel;

	private double attitude;

	private String aiComment;

	private String status;
}
