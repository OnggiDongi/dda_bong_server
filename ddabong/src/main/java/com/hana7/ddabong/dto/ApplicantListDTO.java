package com.hana7.ddabong.dto;

import java.util.List;

import com.hana7.ddabong.enums.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantListDTO {
	// private Long id;
	private String category;
	private String title;
	private String endAt;
	private String imageUrl;
	private int applicantNum;
	private int capacity;

	private List<ApplicantReviewResponseDTO> reviews;

}
