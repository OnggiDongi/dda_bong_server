package com.hana7.ddabong.controller;

import com.hana7.ddabong.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class S3Controller {
	private final S3Service s3Service;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String upload(@RequestBody MultipartFile file) throws IOException {
		return s3Service.uploadFile(file);
	}
}
