package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;


	@BeforeEach
	void setUp() {
	}

	@Test
	void signin() {
		// given
		UserRequestDTO dto = UserRequestDTO.builder()
				.email("byuldori@gmail.com")
				.name("별돌이")
				.password("ItismeByuldol")
				.phoneNumber("01012345678")
				.birthDate("2025-08-22")
				.build();

		// when
		userService.signup(dto);

		User findUser = userRepository.findByEmail(dto.getEmail()).orElse(null);

		// then
		Assertions.assertThat(findUser).isNotNull();
		Assertions.assertThat(findUser.getName()).isEqualTo("별돌이");
	}

	@Test
	void signin_duplicate() {
		// given
		User user = User.builder()
		.email("byuldori@gmail.com")
		.name("별돌이")
		.password("ItismeByuldol")
		.phoneNumber("01012345678")
		.birthdate(LocalDate.now())
		.isKakao(false)
		.build();

		userRepository.save(user);

		UserRequestDTO dto = UserRequestDTO.builder()
				.email("byuldori@gmail.com")
				.name("별돌이")
				.password("ItismeByuldol")
				.phoneNumber("01012345678")
				.birthDate("2025-08-22")
				.build();

		// when + then
		assertThatThrownBy(() -> userService.signup(dto))
				.isInstanceOf(ConflictException.class);
	}
}
