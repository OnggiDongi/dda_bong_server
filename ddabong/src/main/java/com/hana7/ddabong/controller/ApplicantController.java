package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ApplicantDetailResponseDTO;
import com.hana7.ddabong.dto.ApplicantListDTO;
import com.hana7.ddabong.dto.ApplicantRemovalMessage;
import com.hana7.ddabong.dto.ApplicantStatusMessage;
import com.hana7.ddabong.entity.Applicant;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")
@Tag(name = "봉사 지원자")
public class ApplicantController {
	private final ApplicantService applicantService;
	private final SimpMessagingTemplate messagingTemplate;

	@Operation(summary = "기관은 자신이 등록한 봉사 모집글에 봉사 신청한 지원자를 거절할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "해당 지원자를 거절했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 기관 | 지원자가 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
			@ApiResponse(responseCode = "409",
					description = "해당 지원자를 이미 승인 또는 거절했습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
	})
	@PostMapping("/{applicantId}/reject")
	public ResponseEntity<?> rejectApplicant(@PathVariable Long applicantId, Authentication authentication) {
		Long activityPostId = applicantService.rejectApplicant(authentication.getName(), applicantId);
		ApplicantStatusMessage msg = new ApplicantStatusMessage(activityPostId, "REJECTED");
		messagingTemplate.convertAndSend("/topic/applicant-reject/" + activityPostId, msg);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기관은 자신이 등록한 봉사 모집글에 봉사 신청한 지원자를 승인할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "해당 지원자를 승인했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 기관 | 지원자가 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
			@ApiResponse(responseCode = "409",
					description = "해당 지원자를 이미 승인 또는 거절했습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
	})
	@PostMapping("/{applicantId}/accept")
	public ResponseEntity<?> approveApplicant(@PathVariable Long applicantId, Authentication authentication) {
		Long activityPostId = applicantService.approveApplicant(authentication.getName(), applicantId);
		ApplicantStatusMessage msg = new ApplicantStatusMessage(activityPostId, "APPROVED");
		messagingTemplate.convertAndSend("/topic/applicant-accept/" + activityPostId, msg);
		return ResponseEntity.ok().build();
	}

//	@PreAuthorize("hasAnyRole('ROLE_INSTITUTION')")
	@Operation(summary = "기관은 자신의 봉사 모집글에 지원한 지원자 상세정보를 조회할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "해당 지원자의 정보를 조회했습니다.", content = @Content(mediaType = "application/json", schema =  @Schema(implementation = ApplicantDetailResponseDTO.class))),
			@ApiResponse(responseCode = "404",
					description = "해당하는 기관 | 지원자가 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
	})
	@GetMapping("/{userId}")
	public ResponseEntity<?> getApplicantInfo(@PathVariable Long userId) {
		ApplicantDetailResponseDTO applicantInfo = applicantService.getApplicantInfo(userId);
		return ResponseEntity.ok(applicantInfo);
	}

}
