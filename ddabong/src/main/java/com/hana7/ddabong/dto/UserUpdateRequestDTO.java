package com.hana7.ddabong.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private String profileImage;
    private String password;
    private String phoneNumber;
    private String birthDate;
    private String preferredRegion;
    private String preferredCategory;
}
