package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ApplicantDetailResponseDTO;
import com.hana7.ddabong.dto.UserReviewResponseDTO;
import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.Category;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import com.hana7.ddabong.repository.UserRepository;
import com.hana7.ddabong.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final InstitutionRepository	institutionRepository;
	private final UserRepository userRepository;
	private final UserReviewRepository userReviewRepository;

	@Transactional
	public void rejectApplicant(String email, Long applicantId) {
		Institution institution = institutionRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		Applicant applicant = applicantRepository.findById(applicantId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_APPLICANT));


		// 삭제된 기관일 경우
		if (institution.getDeletedAt() != null){
			throw new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION);
		}
		// 신청 취소한 지원자일 경우
		if (applicant.getDeletedAt() != null){
			throw new NotFoundException(ErrorCode.NOTFOUND_APPLICANT);
		}
		// 지원한 봉사활동을 등록한 기관이 아닐 경우
		if(!applicant.getActivityPost().getActivity().getInstitution().equals(institution)){
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}
		// 이미 승인 또는 거절한 지원자일 경우
		if (!applicant.getStatus().equals(ApprovalStatus.PENDING)){
			throw new BadRequestException(ErrorCode.BAD_REQUEST_ALREADY_PROCESSED_APPLICANT);
		}

		applicant = applicant.toBuilder()
				.status(ApprovalStatus.REJECTED)
				.build();
		applicantRepository.save(applicant);
	}

	public ApplicantDetailResponseDTO getApplicantInfo(Long userId){
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		List<UserReview> userReviews = userReviewRepository.findByUserId(userId);

		List<UserReviewResponseDTO> reviewDto = toReviewDto(userReviews);

		double totalRate = reviewDto.stream()
				.mapToDouble(UserReviewResponseDTO::getTotalRate)
				.average().orElse(0);

		// 회원의 종합적인 체력점수
		double totalHealthStatus = reviewDto.stream()
				.mapToInt(UserReviewResponseDTO::getHealthStatus)
				.average().orElse(0);

		// 회원의 종합적인 성실도
		double totalDiligenceLevel = reviewDto.stream()
				.mapToInt(UserReviewResponseDTO::getDiligenceLevel)
				.average().orElse(0);

		// 회원의 종합적인 태도점수
		double totalAttitude = reviewDto.stream()
				.mapToInt(UserReviewResponseDTO::getAttitude)
				.average().orElse(0);

		List<String> strCategories = user.getPreferredCategory().stream()
				.map(Category::getDescription)
				.toList();

		return ApplicantDetailResponseDTO.builder()
				.userName(user.getName())
				.birthDate(formatDate(user.getBirthdate()))
				.phoneNumber(user.getPhoneNumber())
				.profileImage(user.getProfileImage())
				.preferredCategory(strCategories)
//							.reviewSummary("") // TODO : AI 붙이면 넣기
				.totalGrade(formatAverage(totalRate))
				.healthStatus(formatAverage(totalHealthStatus))
				.diligenceLevel(formatAverage(totalDiligenceLevel))
				.attitude(formatAverage(totalAttitude))

				.userReviews(reviewDto)
				.build();
	}

	private List<UserReviewResponseDTO> toReviewDto(List<UserReview> userReviews){
		return userReviews.stream()
				.map(review -> {

					Activity activity = review.getActivityPost().getActivity();
					ActivityPost activityPost = review.getActivityPost();

					double totalRateEachReview = (double) (review.getAttitude() + review.getDiligenceLevel() + review.getHealthStatus()) / 3;
					return UserReviewResponseDTO.builder()
							.id(review.getId())

							.activityTitle(activity.getTitle())
							.activityEndAt(formatDate(LocalDate.from(activityPost.getEndAt())))
							.activityCategory(activity.getCategory().getDescription())
							.activityImage(activityPost.getImageUrl())

							.totalRate(formatAverage(totalRateEachReview))
							.healthStatus(review.getHealthStatus())
							.diligenceLevel(review.getDiligenceLevel())
							.attitude(review.getAttitude())

							.memo(review.getMemo())
							.writeInst(review.getWriteInst())
							.build();


				}).toList();
	}
	private String formatDate(LocalDate birthDate){
		return String.format("%d.%02d.%02d", birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
	}

	private double formatAverage(double value){
		String format = String.format("%.1f", value);
		return Double.parseDouble(format);
	}

}
