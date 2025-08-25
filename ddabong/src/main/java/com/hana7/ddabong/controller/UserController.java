package com.hana7.ddabong.controller;

import java.util.List;

import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.dto.UserUpdateRequestDTO;
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

    @PostMapping("/{id}/onboarding")
    public ResponseEntity<UserResponseDTO> updateUserOnboardingInfo(@PathVariable Long id, @RequestBody UserOnboardingRequestDTO userOnboardingRequestDTO) {
        return ResponseEntity.ok(userService.updateOnboardingInfo(id, userOnboardingRequestDTO));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(Authentication authentication, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        String email = authentication.getName();
        Long id = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(userService.updateUser(id, userUpdateRequestDTO));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<ActivityPostResponseDTO>> getLikedActivities(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findLikedActivities(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ActivityPostResponseDTO>> getActivityHistory(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findActivityHistory(id));
    }

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
