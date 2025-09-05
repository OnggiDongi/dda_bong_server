package com.hana7.ddabong.controller;

import com.hana7.ddabong.dto.ExceptionResponseDTO;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.CustomJwtException;
import com.hana7.ddabong.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.hana7.ddabong.exception.GeminiApiException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler (BadRequestException.class)
	public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(BadRequestException e) {
		ExceptionResponseDTO response = ExceptionResponseDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.errorCode(e.getErrorCode())
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler (ConflictException.class)
	public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(ConflictException e) {
		ExceptionResponseDTO response = ExceptionResponseDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.errorCode(e.getErrorCode())
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler (CustomJwtException.class)
	public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(CustomJwtException e) {
		ExceptionResponseDTO response = ExceptionResponseDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(response);
	}

	@ExceptionHandler (NotFoundException.class)
	public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(NotFoundException e) {
		ExceptionResponseDTO response = ExceptionResponseDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.errorCode(e.getErrorCode())
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(GeminiApiException.class)
	public ResponseEntity<ExceptionResponseDTO> handleGeminiApiException(GeminiApiException e) {
		ExceptionResponseDTO response = ExceptionResponseDTO.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
