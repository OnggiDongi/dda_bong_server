package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
