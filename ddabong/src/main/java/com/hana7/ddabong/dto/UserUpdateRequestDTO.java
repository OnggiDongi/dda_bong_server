package com.hana7.ddabong.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private String password;
    private String phoneNumber;
    private String birthDate;
    private String preferredRegion;
    private String preferredCategory;

    private MultipartFile profileImage;
}
