package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.service.UserService;
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
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@Tag(name = "회원가입")
	@Operation(summary = "모든 사용자는 아이디, 비밀번호, 닉네임을 통해 회원가입을 할 수 있다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "400",
					description = "생년월일이 현재보다 미래일 수 없습니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
			@ApiResponse(responseCode = "409",
					description = "이미 존재하는 회원입니다.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConflictException.class)))
	})
	@PostMapping("/signup")
	public ResponseEntity<?> signUser(@Validated @RequestBody UserRequestDTO userRequestDTO){
		userService.signup(userRequestDTO);
		return ResponseEntity.ok().build();
	}
}
