package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.Institution;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class InstitutionRequestDTO {

	@NotBlank(message = "기관명을 입력해주세요.")
	@Size(min = 1, max = 30, message = "30자 이내로 입력해주세요.")
	private String name;

	@NotBlank(message = "아이디(이메일)을 입력해주세요.")
	@Size(min = 1, max = 30, message = "30자 이내로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	private String phoneNumber;

	public Institution toEntity() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();

		String encodedPassword = encoder.encode(password);
		return Institution.builder()
				.name(name)
				.email(email)
				.password(encodedPassword)
				.phoneNumber(phoneNumber)
				.build();

	}

}


