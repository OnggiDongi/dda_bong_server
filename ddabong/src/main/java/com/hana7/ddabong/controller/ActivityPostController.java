package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.*;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.ActivityPostService;
import com.hana7.ddabong.service.ApplicantService;
import com.hana7.ddabong.service.LikesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ActivityPostController {
	private final ActivityPostService activityPostService;
	private final LikesService likesService;
	private final ApplicantService applicantService;

	@Tag(name = "봉사활동 게시물 API")
	@Operation(summary = "게시물 등록")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createActivityPost(
			@Valid
			ActivityPostRequestDTO dto,
			Authentication authentication
	) throws IOException {
		activityPostService.createActivityPost(dto, authentication.getName());
		return ResponseEntity.ok().build();
	}

	@Tag(name = "봉사활동 게시물 API")
	@Operation(summary = "게시물 조회(리스트)")
	@GetMapping(value = "")
	public ResponseEntity<List<ActivityPostResponseDTO>> readActivityPostList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(required = false) String searchRegion,
			@RequestParam(required = false) String categories,
			Authentication authentication
	) {
		return activityPostService.readActivityPostList(page, pageSize, searchRegion, categories, authentication.getName());
	}

	@Tag(name = "봉사활동 게시물 API")
	@Operation(summary = "게시물 수정")
	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateActivityPost(
			@PathVariable Long id,
			ActivityPostRequestDTO dto,
			Authentication authentication
	) throws IOException {
		activityPostService.updateActivityPost(id, dto, authentication.getName());
		return ResponseEntity.ok().build();
	}

	@Tag(name = "봉사활동 게시물 API")
	@Operation(summary = "게시물 삭제")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteActivityPost(
			@PathVariable Long id,
			Authentication authentication
	) {
		activityPostService.deleteActivityPost(id, authentication.getName());
		return ResponseEntity.ok().build();
	}

	@Tag(name = "봉사활동 게시물 API")
	@Operation(summary = "봉사 모집 마감하기")
	@PostMapping(value = "/{id}")
	public ResponseEntity<?> closeActivityPost(
			@PathVariable Long id,
			Authentication authentication
	) {
		activityPostService.closeActivityPost(id, authentication.getName());
		return ResponseEntity.ok().build();
	}

	@Tag(name = "봉사 모집글 상세보기")
	@Operation(summary = "기관이 작성한 봉사 모집글에 대한 상세정보를 확인할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 조회에 성공했습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityPostDetailResponseDTO.class))),
			@ApiResponse(responseCode = "404",
					description = "해당하는 봉사 모집글이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@GetMapping("/{activityPostId}")
	public ResponseEntity<ActivityPostDetailResponseDTO> getActivityPost(@PathVariable Long activityPostId) {
		ActivityPostDetailResponseDTO dto = activityPostService.getPost(activityPostId);
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


	@Tag(name = "봉사 모집글 지원 취소하기")
	@Operation(summary = "일반 유저는 봉사 모집글 지원을 취소할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 조회에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 회원 또는 봉사 모집글이 존재하지 않습니다. || 회원은 해당 모집글에 지원한 이력이 없습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
			@ApiResponse(responseCode = "409",
					description = "해당 지원 상태가 대기중일 경우에만 지원 취소가 가능합니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
	})
	@PostMapping("/{activityPostId}/reject")
	public ResponseEntity<?> rejectActivityPost(@PathVariable Long activityPostId, Authentication authentication) {
		activityPostService.cancelActivity(authentication.getName(), activityPostId);
		return ResponseEntity.ok().build();
	}

	@Tag(name = "자신이 작성한 봉사 모집글 조회하기")
	@Operation(summary = "기관은 자신이 작성한 봉사 모집글을 조회할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 조회에 성공했습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyActivityPostResponseDTO.class))),
			@ApiResponse(responseCode = "404",
					description = "해당하는 기관이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
	})
	@GetMapping("/myposts")
	public ResponseEntity<List<MyActivityPostResponseDTO>> getActivityPost(@RequestParam(required = false, defaultValue = "true") boolean isRecruting, Authentication authentication) {
		List<MyActivityPostResponseDTO> getMyActivityPosts = activityPostService.getMyActivityPosts(authentication.getName(), isRecruting);
		return ResponseEntity.ok(getMyActivityPosts);
	}

	@Tag(name = "봉사 모집글 찜하기 & 취소하기")
	@Operation(summary = "회원은 봉사 모집글을 찜하거나 찜한 것을 취소할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "봉사 모집글 찜하기에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "해당하는 기관 | 회원이 존재하지 않습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
	})
	@PostMapping("/{activityPostId}/like")
	public ResponseEntity<?> likeActivityPost(@PathVariable Long activityPostId, Authentication authentication) {
		likesService.likeActivityPost(authentication.getName(), activityPostId);
		return ResponseEntity.ok().build();
	}

	@Tag(name = "봉사 지원자 목록 확인하기")
	@Operation(summary = "기관은 자신이 등록한 봉사 모집글에 봉사 신청한 지원자 목록을 확인할 수 있다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "해당 봉사 모집글에 지원한 지원자 목록입니다.", content = @Content(mediaType = "application/json", schema =  @Schema(implementation = ApplicantListDTO.class))),
		@ApiResponse(responseCode = "404",
			description = "해당하는 기관 | 봉사 모집글이 존재하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
	})
	@GetMapping("/apply/{activityPostId}")
	public ResponseEntity<ApplicantListDTO> getApplicantList(
		@PathVariable Long activityPostId,
		Authentication authentication
	) {
		return ResponseEntity.ok(applicantService.getApplicants(authentication.getName(), activityPostId));
	}

}
