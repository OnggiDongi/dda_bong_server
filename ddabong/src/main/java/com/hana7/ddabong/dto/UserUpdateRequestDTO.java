package com.hana7.ddabong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 1, max = 20, message = "1자 이상 20자 이내로 입력해주세요.")
    private String name;

    @NotBlank(message = "010-1234-5678 형식으로 입력하세요.")
    @Size(min = 1, max = 13, message = "010-1234-5678 형식으로 입력하세요.")
    private String phoneNumber;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 1, max = 60, message = "1자 이상 60자 이내로 입력해주세요.")
    private String password;

    @NotBlank(message = "생년월일은 필수입니다.")
    @Size(min = 1, max = 30, message = "입력해주세요.")
    private LocalDate birthdate;
}
