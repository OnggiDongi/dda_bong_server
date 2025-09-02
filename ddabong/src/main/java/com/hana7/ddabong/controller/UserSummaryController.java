package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserSummaryResponseDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.UserSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보 요약을 성공적으로 조회했습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404",
            description = "해당하는 유저가 존재하지 않습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
        @ApiResponse(responseCode = "400",
            description = "잘못된 요청입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    @GetMapping("/summary")
    public ResponseEntity<UserSummaryResponseDTO> getUserSummaryByEmail(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userSummaryService.findUserSummaryByEmail(email));    }
}
