package com.hana7.ddabong.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

	@NotBlank(message = "일반 회원의 이름을 입력해주세요.")
	@Size(min = 1, max = 20, message = "20자 이내로 입력해주세요.")
	private String name;

	@Email
	@NotBlank(message = "일반 회원의 아이디(이메일)을 입력해주세요.")
	@Size(min = 1, max = 30, message = "30자 이내로 입력해주세요.")
	private String email;

	@NotBlank(message = "일반 회원의 비밀번호을 입력해주세요.")
	private String password;

	@Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	private String phoneNumber;

	@Pattern(
			regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$",
			message = "생년월일은 YYYY-MM-DD 형식이어야 합니다."
	)
	private String birthDate;
}
