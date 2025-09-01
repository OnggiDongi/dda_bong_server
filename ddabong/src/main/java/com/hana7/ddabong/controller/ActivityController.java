package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityRequestDTO;
import com.hana7.ddabong.dto.ActivityResponseDTO;
import com.hana7.ddabong.dto.ActivityUpdateDTO;
import com.hana7.ddabong.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
@Log4j2
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping()
    public ResponseEntity<?> createActivity(@RequestBody ActivityRequestDTO dto, Authentication authentication) {
        activityService.createActivity(dto, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<ActivityResponseDTO>> readActivityList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String keyWord,
            Authentication authentication
    ) {
        return activityService.readActivityList(page, pageSize, keyWord, authentication.getName());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ActivityResponseDTO> readActivity(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return activityService.readActivity(id, authentication.getName());
    }

    @PatchMapping()
    public ResponseEntity<?> updateActivity(
            @RequestBody ActivityUpdateDTO dto,
            Authentication authentication
    ) {
        activityService.updateActivity(dto, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id,
            Authentication authentication
    ) {
        activityService.deleteActivity(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
