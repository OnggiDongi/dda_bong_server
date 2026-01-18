package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "testuser@example.com";

    @Test
    @Order(1)
    void saveUserTest() {
        userRepository.findByEmail(TEST_EMAIL).ifPresent(user -> userRepository.delete(user));

        User newUser = User.builder()
                .name("테스트유저")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode("password123"))
                .phoneNumber("010-9999-8888")
                .birthdate(LocalDate.of(1995, 5, 10))
                .isKakao(false)
                .totalHour(0)
                .build();

        userRepository.save(newUser);

        Optional<User> foundUserOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(foundUserOptional).isPresent();
    }

    @Test
    @Order(2)
    void updateUserNameTest() {
        Optional<User> userOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(userOptional).as("이전 테스트에서 사용자가 저장되어 있어야 합니다.").isPresent();
        User user = userOptional.get();
        String updatedName = "수정된유저";

        user.setName(updatedName);
        userRepository.save(user);

        Optional<User> updatedUserOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(updatedUserOptional).isPresent();
        assertThat(updatedUserOptional.get().getName()).isEqualTo(updatedName);
    }

    @Test
    @Order(3)
    void updateUserPasswordTest() {

        Optional<User> userOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(userOptional).as("이전 테스트에서 사용자가 저장되어 있어야 합니다.").isPresent();
        User user = userOptional.get();
        String newRawPassword = "newPassword123!";

        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);

        Optional<User> updatedUserOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(updatedUserOptional).isPresent();
        //matches
        assertThat(passwordEncoder.matches(newRawPassword, updatedUserOptional.get().getPassword())).isTrue();
    }

    @Test
    @Order(4)
    void deleteUserTest() {
        Optional<User> userOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(userOptional).as("수정 테스트에서 사용자가 남아있어야 합니다.").isPresent();

        userRepository.delete(userOptional.get());

        Optional<User> deletedUserOptional = userRepository.findByEmail(TEST_EMAIL);
        assertThat(deletedUserOptional).isNotPresent();
    }
}
