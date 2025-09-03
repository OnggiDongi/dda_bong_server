package com.hana7.ddabong.controller;

import java.util.List;
import java.util.Map;

import com.hana7.ddabong.auth.JwtProvider;
import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.dto.UserSummaryResponseDTO;
import com.hana7.ddabong.dto.UserUpdateRequestDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

	@GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("/onboarding")
    public ResponseEntity<?> updateUserOnboardingInfo(@RequestBody UserOnboardingRequestDTO userOnboardingRequestDTO, Authentication authentication) {
		userService.updateOnboardingInfo(authentication.getName(), userOnboardingRequestDTO);
		return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(Authentication authentication, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateUser(email, userUpdateRequestDTO));
    }

    @GetMapping("/likes")
    public ResponseEntity<List<ActivityPostResponseDTO>> getLikedActivities(Authentication authentication) {
        return ResponseEntity.ok(userService.findLikedActivities(authentication.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ActivityPostResponseDTO>> getActivityHistory(Authentication authentication) {
        return ResponseEntity.ok(userService.findActivityHistory(authentication.getName()));
    }

	@Tag(name = "회원가입 - 일반회원")
	@Operation(summary = "모든 사용자는 아이디, 비밀번호, 이름, 전화번호, 생년월일을 통해 회원가입을 할 수 있다.")
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

	@GetMapping("/login/kakao")
	public ResponseEntity<Map<String, Object>> loginKakao(Authentication authentication) {
		Map<String, Object> response = JwtProvider.getClaims(authentication);
		System.out.println("response = " + response);
		return ResponseEntity.ok(response);
	}

	@Tag(name = "이메일로 유저 정보 요약 조회")
	@Operation(summary = "유저의 이메일로 유저 정보(이름, 등급, 누적 봉사시간) 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "유저 정보 요약을 성공적으로 조회했습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSummaryResponseDTO.class))),
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
		return ResponseEntity.ok(userService.findUserSummaryByEmail(email));
	}
}
