package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummaryResponseDTO {
    private String name;
    private String grade;
    private int totalHour;
}
