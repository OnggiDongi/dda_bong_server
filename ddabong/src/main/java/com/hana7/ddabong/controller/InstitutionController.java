package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.InstitutionRequestDTO;
import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.service.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institutions")
@RequiredArgsConstructor
public class InstitutionController {

	private final InstitutionService institutionService;

	@Tag(name = "회원가입- 기관")
	@Operation(summary = "모든 기관 아이디, 비밀번호, 기관명, 전화번호를 통해 회원가입을 할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "409",
					description = "이미 존재하는 기관입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConflictException.class)))
	})
	@PostMapping("/signup")
	public ResponseEntity<?> signUser(@Validated @RequestBody InstitutionRequestDTO institutionRequestDTO){
		institutionService.signUp(institutionRequestDTO);
		return ResponseEntity.ok().build();
	}
}
