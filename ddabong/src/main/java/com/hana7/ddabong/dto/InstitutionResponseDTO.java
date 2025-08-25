package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstitutionResponseDTO {

	private Long id;

	private String name;

	private String email;

	private String phoneNumber;

	private String detail;
}
