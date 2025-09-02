package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationResponseDTO {
    private Long id;
    private String username;
    private int hour;
    private LocalDateTime issuedAt;

    public static CertificationResponseDTO from(Certification certification, String userName) {
        return CertificationResponseDTO.builder()
                .id(certification.getId())
                .hour(certification.getHour())
                .username(userName)
                .issuedAt(certification.getCreatedAt())
                .build();
    }
}
