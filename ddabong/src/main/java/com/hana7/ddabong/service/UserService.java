package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public void signup(UserRequestDTO userRequestDTO) {
		// 같은 아이디 있는지 확인하기
		User user = userRepository.findByEmail(userRequestDTO.getEmail())
				.orElse(null);

		if(user == null) { // 해당 이메일로 등록된 회원이 없을 경우 회원가입 처리
			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());

			// 생년월일 String -> LocalDate
			LocalDate birthDate = LocalDate.parse(userRequestDTO.getBirthDate());

			if(birthDate.isAfter(LocalDate.now())) { // 생년월일이 현재보다 미래일 경우
				throw new BadRequestException(ErrorCode.BAD_REQUEST_FUTURE_BIRTHDATE);
			}

			User newUser = User.builder()
					.email(userRequestDTO.getEmail())
					.password(encodedPassword)
					.name(userRequestDTO.getName())
					.phoneNumber(userRequestDTO.getPhoneNumber())
					.birthdate(birthDate)
					.isKakao(false)
					.build();

			userRepository.save(newUser);
			return;
		}

		// 이미 있는 회원일 경우
		throw new ConflictException(ErrorCode.CONFLICT_USER);
	}
}
