package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.InstitutionRequestDTO;
import com.hana7.ddabong.dto.InstitutionResponseDTO;
import com.hana7.ddabong.dto.InstitutionSummaryResponseDTO;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.InstitutionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

	@Tag(name = "기관 정보 조회하기")
	@Operation(summary = "기관의 정보를 확인할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기관 조회를 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "존재하지 않는 기관입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@GetMapping("/{id}")
	public ResponseEntity<InstitutionResponseDTO> getInstitutionInfo(@PathVariable Long id){
		InstitutionResponseDTO institutionInfo = institutionService.getInstitutionInfo(id);
		return ResponseEntity.status(HttpStatus.OK).body(institutionInfo);
	}

	@Tag(name = "기관 정보 수정하기")
	@Operation(summary = "기관이 자신의 정보를 수정할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기관 정보 수정을 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "존재하지 않는 기관입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@PatchMapping("")
	public ResponseEntity<?> getInstitutionInfo(Authentication authentication, @Validated @RequestBody InstitutionRequestDTO institutionRequestDTO){
		String email = authentication.getName();
		institutionService.update(email, institutionRequestDTO);
		return ResponseEntity.ok().build();
	}

	@Tag(name = "기관 탈퇴하기")
	@Operation(summary = "기관이 탈퇴할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기관 탈퇴에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404",
					description = "존재하지 않는 기관입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@DeleteMapping("")
	public ResponseEntity<?> getInstitutionInfo(Authentication authentication){
		String email = authentication.getName();
		institutionService.delete(email);
		return ResponseEntity.ok().build();
	}

	@Tag(name= "기관명 이메일로 조회하기")
	@Operation(summary = "기관명을 이메일로 조회할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기관명 조회를 성공했습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstitutionSummaryResponseDTO.class))),
			@ApiResponse(responseCode = "404",
					description = "존재하지 않는 기관입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	})
	@GetMapping("/summary")
	public ResponseEntity<InstitutionSummaryResponseDTO> getInstitutionSummaryByEmail(Authentication authentication){
		String email = authentication.getName();
		return ResponseEntity.ok(institutionService.findInstitutionSummaryByEmail(email
		));
	}
}
