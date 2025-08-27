package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ActivityPostRequestDTO;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.service.ActivityPostService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/activityPost")
@RequiredArgsConstructor
@Log4j2
public class ActivityPostController {

    private final ActivityPostService activityPostService;

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
    @Operation(summary = "게시물 수정")
    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateActivityPost() {
        return ResponseEntity.ok().build();
    }
}
