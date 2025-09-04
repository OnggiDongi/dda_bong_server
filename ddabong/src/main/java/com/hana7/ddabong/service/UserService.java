package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.dto.UserSummaryResponseDTO;
import com.hana7.ddabong.dto.UserUpdateRequestDTO;
import com.hana7.ddabong.entity.Applicant;
import com.hana7.ddabong.entity.Likes;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.Category;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.LikesRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
	private final PasswordEncoder passwordEncoder;


    public UserResponseDTO findUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));
		String grade = user.getTotalHour() < 111 ? "SILVER" : "VIP";

		return UserResponseDTO.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.totalHour(user.getTotalHour())
			.birthdate(user.getBirthdate())
			.preferredRegion(user.getPreferredRegion()!= null
				? user.getPreferredRegion()
				: null
					)
			.profileImage(user.getProfileImage()!= null
				? user.getProfileImage()
				: "https://ddabong-upload.s3.ap-northeast-2.amazonaws.com/uploads/7edb4d83-5813-4032-8292-e9f73c086474-(Frame 2087326976.png)"
					)
			.preferredCategory(user.getPreferredCategory()!= null
				? user.getPreferredCategory().getDescription()
				: null
					)
			.grade(grade)
			.build();
    }

    @Transactional
    public void updateOnboardingInfo(String email, UserOnboardingRequestDTO userOnboardingRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        user = user.toBuilder()
                .preferredRegion(userOnboardingRequestDTO.getPreferredRegion())
                .preferredCategory(Category.fromDescription(userOnboardingRequestDTO.getPreferredCategory()))
                .build();

        userRepository.save(user);
    }

	@Transactional
	public UserResponseDTO updateUser(String email, UserUpdateRequestDTO userUpdateRequestDTO) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		User updated = user.toBuilder()
				.password(userUpdateRequestDTO.getPassword() != null
						? passwordEncoder.encode(userUpdateRequestDTO.getPassword())
						: user.getPassword())
				.phoneNumber(userUpdateRequestDTO.getPhoneNumber() != null
						? userUpdateRequestDTO.getPhoneNumber()
						: user.getPhoneNumber())
				.birthdate(userUpdateRequestDTO.getBirthDate() != null
						? LocalDate.parse(userUpdateRequestDTO.getBirthDate(), fmt)
						: user.getBirthdate())
				.profileImage(userUpdateRequestDTO.getProfileImage() != null
						? userUpdateRequestDTO.getProfileImage()
						: user.getProfileImage())
				.preferredRegion(userUpdateRequestDTO.getPreferredRegion() != null
						? userUpdateRequestDTO.getPreferredRegion()
						: user.getPreferredRegion())
				.preferredCategory(userUpdateRequestDTO.getPreferredCategory() != null
						? Category.fromDescription(userUpdateRequestDTO.getPreferredCategory())
						: user.getPreferredCategory())
				.build();

		userRepository.save(updated);
		return toDTO(updated);
	}

	private boolean hasText(String s) {
		return s != null && !s.isBlank();
	}

    public List<ActivityPostResponseDTO> findLikedActivities(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        List<Likes> likes = likesRepository.findByUserAndDeletedAtIsNull(user);

        return likes.stream()
                .map(like -> ActivityPostResponseDTO.of(like.getActivityPost()))
                .collect(Collectors.toList());
    }

    public List<ActivityPostResponseDTO> findActivityHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));
        List<Applicant> applicants = applicantRepository.findByUser(user);
        return applicants.stream()
                .map(applicant -> ActivityPostResponseDTO.of(applicant.getActivityPost()))
                .collect(Collectors.toList());
    }


    private UserResponseDTO toDTO(User user) {
		String grade = user.getTotalHour() < 111 ? "SILVER" : "VIP";

		return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalHour(user.getTotalHour())
                .birthdate(user.getBirthdate())
                .preferredRegion(user.getPreferredRegion())
                .profileImage(user.getProfileImage())
				.preferredCategory(
					user.getPreferredCategory() != null
					? user.getPreferredCategory().getDescription()
					: null)
				.grade(grade)
                .build();
    }


    @Transactional
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
					.profileImage("https://ddabong-upload.s3.ap-northeast-2.amazonaws.com/uploads/7edb4d83-5813-4032-8292-e9f73c086474-(Frame 2087326976.png)")
					.isKakao(false)
					.build();

			userRepository.save(newUser);
			return;
		}

		// 이미 있는 회원일 경우
		throw new ConflictException(ErrorCode.CONFLICT_USER);
	}

	@Transactional
	public UserSummaryResponseDTO findUserSummaryByEmail(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		String grade = user.getTotalHour() < 111 ? "SILVER" : "VIP";

		return UserSummaryResponseDTO.builder()
			.name(user.getName())
			.grade(grade)
			.totalHour(user.getTotalHour())
			.build();
	}
}
