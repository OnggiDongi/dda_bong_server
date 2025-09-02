package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserSummaryResponseDTO;
import com.hana7.ddabong.service.UserSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserSummary", description = "유저 정보 요약 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserSummaryController {

    private final UserSummaryService userSummaryService;

    @Tag(name = "이메일로 유저 정보 요약 조회")
    @Operation(summary = "유저의 이메일로 유저 정보(이름, 등급, 누적 봉사시간) 조회")
    @GetMapping("/summary")
    public ResponseEntity<UserSummaryResponseDTO> getUserSummaryByEmail(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userSummaryService.findUserSummaryByEmail(email));    }
}
