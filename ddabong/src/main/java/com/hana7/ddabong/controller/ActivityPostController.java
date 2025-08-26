package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityPostDetailResponseDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
