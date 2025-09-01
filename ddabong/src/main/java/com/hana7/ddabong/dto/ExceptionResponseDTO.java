package com.hana7.ddabong.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionResponseDTO {
	private HttpStatus status;
	private int errorCode;
	private String errorMessage;

}
