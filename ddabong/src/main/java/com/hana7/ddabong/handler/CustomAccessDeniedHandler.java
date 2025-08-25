package com.hana7.ddabong.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		PrintWriter out = response.getWriter();
		out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESSDENIED")));
		out.close();
	}
}
