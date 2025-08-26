package com.hana7.ddabong.config;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Server devServer = new Server();
		Server prodServer = new Server();
		devServer.setUrl("/");
		prodServer.setUrl("/api");

		return new OpenAPI()
				.servers(List.of(devServer, prodServer))
				.info(getInfo())
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new Components()
						.addSecuritySchemes("bearerAuth",
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
						)
				);
	}

	private Info getInfo() {
		return new Info()
				.version("0.1.0")
				.title("SpringDemo APIs")
				.description("DDABONG Project API Documents");
	}

	@Bean
	public GroupedOpenApi usersApi() {
		return GroupedOpenApi.builder()
				.group("users")                 // UI에서 보일 그룹명
				.pathsToMatch("/users/**")      // 이 경로 하위만 포함
				.build();
	}

	@Bean
	public GroupedOpenApi institutionsApi() {
		return GroupedOpenApi.builder()
				.group("institutions")
				.pathsToMatch("/institutions/**")
				.build();
	}

	@Bean
	public GroupedOpenApi certificationApi() {
		return GroupedOpenApi.builder()
			.group("certifications")
			.pathsToMatch("/certifications/**")
			.build();
	}
	@Bean
	public GroupedOpenApi activityApi() {
		return GroupedOpenApi.builder()
			.group("activity")
			.pathsToMatch("/activity/**")
			.build();
	}
}
