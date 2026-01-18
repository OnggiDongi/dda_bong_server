package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ApplicantDetailResponseDTO;
import com.hana7.ddabong.dto.ApplicantListDTO;
import com.hana7.ddabong.dto.ApplicantReviewResponseDTO;
import com.hana7.ddabong.dto.UserReviewResponseDTO;
import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.Category;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityPostRepository;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import com.hana7.ddabong.repository.UserRepository;
import com.hana7.ddabong.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final InstitutionRepository	institutionRepository;
	private final UserRepository userRepository;
	private final UserReviewRepository userReviewRepository;
	private final ActivityPostRepository activityPostRepository;
	private final UserReviewSummaryService userReviewSummaryService;

	@Transactional
	public Map<String, Long> rejectApplicant(String email, Long applicantId) {
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

		Long activityPostId = applicant.getActivityPost().getId();
		Long userId = applicant.getUser().getId();

		Map<String, Long> map = new HashMap<>();
		map.put("activityPostId", activityPostId);
		map.put("userId", userId);

		return map;
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

		// TODO : AIComment
		String aiComment = userReviewSummaryService.summarizeForUser(user.getId());
		return ApplicantDetailResponseDTO.builder()
				.userName(user.getName())
				.birthDate(formatDate(user.getBirthdate()))
				.phoneNumber(user.getPhoneNumber())
				.profileImage(user.getProfileImage())
				.preferredCategory(user.getPreferredCategory().getDescription())
				.totalGrade(formatAverage(totalRate))
				.healthStatus(formatAverage(totalHealthStatus))
				.diligenceLevel(formatAverage(totalDiligenceLevel))
				.attitude(formatAverage(totalAttitude))
				.reviewSummary(aiComment)
//				.reviewSummary("ai 연결 중지")
				.userReviews(reviewDto)
				.build();
	}

	public ApplicantListDTO getApplicants(String email, Long activityPostId) {
		Institution institution = institutionRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		ActivityPost activityPost = activityPostRepository.findById(activityPostId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

		if (!Objects.equals(institution.getId(), activityPost.getActivity().getInstitution().getId())){
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}

		List<Applicant> applicants = applicantRepository.findByActivityPostIdAndDeletedAtIsNull(activityPostId);

		// 1. 모든 지원자의 리뷰를 한번에 조회
		List<Long> userIds = applicants.stream().map(a -> a.getUser().getId()).toList();
		List<UserReview> allReviews = userReviewRepository.findByUserIdIn(userIds);

		// 2. 사용자 ID별로 리뷰를 그룹화
		Map<Long, List<UserReview>> reviewsByUserId = allReviews.stream()
				.collect(Collectors.groupingBy(review -> review.getUser().getId()));

		// 3. AI 요약 일괄 요청
		// TODO : AIComment
		Map<Long, String> summaries = userReviewSummaryService.summarizeForMultipleUsers(reviewsByUserId);

		List<ApplicantReviewResponseDTO> list = applicants.stream().map(applicant -> {
					User user = applicant.getUser();
					List<UserReview> userReviews = reviewsByUserId.getOrDefault(user.getId(), Collections.emptyList());

			boolean hasReview = userReviewRepository.existsByUserIdAndActivityPost_Id(user.getId(), activityPostId);

			double avgHealth = userReviews.stream().mapToDouble(UserReview::getHealthStatus).average().orElse(0.0);
					double avgDiligence = userReviews.stream().mapToDouble(UserReview::getDiligenceLevel).average().orElse(0.0);
					double avgAttitude = userReviews.stream().mapToDouble(UserReview::getAttitude).average().orElse(0.0);
					double totalRate = (avgHealth + avgDiligence + avgAttitude) / 3.0;

					return ApplicantReviewResponseDTO.builder()
							.id(applicant.getId())
							.userId(applicant.getUser().getId())
							.name(user.getName())
							.rate(formatAverage(totalRate))
//							.aiComment("ai comment 연결 중지")
							.aiComment(summaries.getOrDefault(user.getId(), "리뷰가 없습니다."))
							.diligenceLevel(formatAverage(avgDiligence))
							.healthStatus(formatAverage(avgHealth))
							.attitude(formatAverage(avgAttitude))
							.status(applicant.getStatus().toString())
							.profileImage(user.getProfileImage())
							.hasReview(hasReview)
							.build();
				}
		).toList();

		return ApplicantListDTO.builder()
				.category(activityPost.getActivity().getCategory().getDescription())
				.title(activityPost.getTitle())
				.endAt(String.format("%d.%02d.%02d",
						activityPost.getEndAt().getYear(),
						activityPost.getEndAt().getMonthValue(),
						activityPost.getEndAt().getDayOfMonth()))
				.imageUrl(activityPost.getImageUrl())
				.applicantNum(activityPost.getApplicants().size())
				.capacity(activityPost.getCapacity())
				.reviews(list)
				.build();
	}

	@Transactional
	public Map<String, Long> approveApplicant(String email, Long applicantId) {
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
				.status(ApprovalStatus.APPROVED)
				.build();
		applicantRepository.save(applicant);

		Long activityPostId = applicant.getActivityPost().getId();
		Long userId = applicant.getUser().getId();

		Map<String, Long> map = new HashMap<>();
		map.put("activityPostId", activityPostId);
		map.put("userId", userId);

		return map;
	}

	private List<UserReviewResponseDTO> toReviewDto(List<UserReview> userReviews){
		System.out.println("userReviews = " + userReviews);
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

	public double formatAverage(double value){
		String format = String.format("%.1f", value);
		return Double.parseDouble(format);
	}

}
