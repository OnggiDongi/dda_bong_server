package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.dto.UserUpdateRequestDTO;
import com.hana7.ddabong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



}
