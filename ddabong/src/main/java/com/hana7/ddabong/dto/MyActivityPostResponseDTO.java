package com.hana7.ddabong.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public class MyActivityPostResponseDTO extends  ActivityPostResponseDTO {
	private double totalAvgScore;
	private int capacity;
}
