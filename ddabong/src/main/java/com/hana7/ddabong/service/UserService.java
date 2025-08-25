package com.hana7.ddabong.service;

<<<<<<< HEAD
import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.dto.UserUpdateRequestDTO;
import com.hana7.ddabong.entity.Applicant;
import com.hana7.ddabong.entity.Likes;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.LikesRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final ApplicantRepository applicantRepository;

    public Long getUserIdByEmail(String email) {
        return userRepository.findIdByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 유저가 없습니다: " + email));
    }

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + id + "인 회원이 없습니다"));
        return toDTO(user);
    }

    @Transactional
    public UserResponseDTO updateOnboardingInfo(Long id, UserOnboardingRequestDTO userOnboardingRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + id + "인 회원이 없습니다"));

        user = user.toBuilder()
                .preferredRegion(userOnboardingRequestDTO.getPreferredRegion())
                .preferredCategory(userOnboardingRequestDTO.getPreferredCategory())
                .build();

        userRepository.save(user);

        return toDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateReq) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id : " + id + "인 회원이 없습니다"));

        User updatedUser = user.toBuilder()
            .name(userUpdateReq.getName())
            .phoneNumber(userUpdateReq.getPhoneNumber())
            .password(userUpdateReq.getPassword()) // TODO: password encoding~~~
            .build();

        userRepository.save(updatedUser);

        return toDTO(updatedUser);
    }

    public List<ActivityPostResponseDTO> findLikedActivities(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + userId + "인 회원이 없습니다"));
        List<Likes> likes = likesRepository.findByUser(user);
        return likes.stream()
                .map(like -> ActivityPostResponseDTO.of(like.getActivityPost()))
                .collect(Collectors.toList());
    }

    public List<ActivityPostResponseDTO> findActivityHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + userId + "인 회원이 없습니다"));
        List<Applicant> applicants = applicantRepository.findByUser(user);
        return applicants.stream()
                .map(applicant -> ActivityPostResponseDTO.of(applicant.getActivityPost()))
                .collect(Collectors.toList());
    }


    private UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalHour(user.getTotalHour())
                .birthdate(user.getBirthdate())
                .isKakao(user.isKakao())
                .preferredRegion(user.getPreferredRegion())
                .profileImage(user.getProfileImage())
                .preferredCategory(user.getPreferredCategory().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .build();
    }

=======
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
	private final PasswordEncoder passwordEncoder;

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
>>>>>>> 9c2f849b2a16ebf6f963ede911ea10c4bfe98f78
}
