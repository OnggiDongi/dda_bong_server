package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private int totalHour;
    private LocalDate birthdate;
    private String preferredRegion;
    private String profileImage;
    private String preferredCategory;
    private String grade;
    
}
