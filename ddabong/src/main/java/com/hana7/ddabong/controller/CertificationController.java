package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.CertificationResponseDTO;
import com.hana7.ddabong.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
@Tag(name = "Certification", description = "인증서 API")
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping("/")
    @Operation(summary = "내 인증서 목록 조회", description = "현재 로그인한 사용자가 보유한 모든 인증서 목록을 조회합니다.")
    public ResponseEntity<List<CertificationResponseDTO>> getMyCertifications(Authentication authentication) {
        List<CertificationResponseDTO> certifications = certificationService.getCertifications(authentication.getName());
        return ResponseEntity.ok(certifications);
    }

    @GetMapping("/{certificationId}")
    @Operation(summary = "내 인증서 상세 조회", description = "특정 인증서의 상세 정보를 조회합니다. (본인 소유 인증서만 가능)")
    public ResponseEntity<CertificationResponseDTO> getMyCertificationById(@PathVariable Long certificationId, Authentication authentication) {
        CertificationResponseDTO certification = certificationService.getCertification(certificationId, authentication.getName());
        return ResponseEntity.ok(certification);
    }
}
