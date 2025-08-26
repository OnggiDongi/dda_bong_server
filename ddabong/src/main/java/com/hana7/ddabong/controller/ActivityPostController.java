package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityPostDetailResponseDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.ActivityPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ActivityPostController {
	private final ActivityPostService activityPostService;

	@Tag(name = "봉사 모집글 상세보기")
	@Operation(summary = "기관이 작성한 봉사 모집글에 대한 상세정보를 확인할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 조회에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 봉사 모집글이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@GetMapping("/{id}")
	public ResponseEntity<ActivityPostDetailResponseDTO> getActivityPost(@PathVariable Long id) {
		ActivityPostDetailResponseDTO dto = activityPostService.getPost(id);
		return ResponseEntity.ok(dto);
	}


	@Tag(name = "봉사 모집글 지원하기")
	@Operation(summary = "일반 유저는 봉사 모집글에 지원할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 조회에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 회원이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
			@ApiResponse(responseCode = "404",
					description = "해당하는 봉사 모집글이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
			@ApiResponse(responseCode = "409",
					description = "이미 해당 회원은 봉사 모집글에 신청했습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
			@ApiResponse(responseCode = "409",
					description = "해당하는 봉사 모집글이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
	})
	@PostMapping("/{activityPostId}/apply")
	public ResponseEntity<?> applyActivityPost(@PathVariable Long activityPostId, Authentication authentication) {
		activityPostService.applyActivity(authentication.getName(), activityPostId);
		return ResponseEntity.ok().build();
	}

}
