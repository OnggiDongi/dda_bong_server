package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.*;
import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.Category;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActivityPostService {
	private final ActivityPostRepository activityPostRepository;
	private final ActivityRepository activityRepository;
	private final ActivityReviewRepository activityReviewRepository;
	private final UserRepository userRepository;
	private final InstitutionRepository institutionRepository;
	private final ApplicantRepository applicantRepository;
	private final ActivityPostCustomRepository activityPostCustomRepository;

	private final S3Service s3Service;

	@Transactional
	public void createActivityPost(ActivityPostRequestDTO dto, String userEmail) throws IOException {
		Institution institution = institutionRepository.findByEmail(userEmail)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
		Activity activity = activityRepository.findById(dto.getActivityId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));


		if (!activity.getInstitution().getId().equals(institution.getId())) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime startAt = LocalDateTime.parse(dto.getStartAt(), formatter);
			LocalDateTime recruitmentEnd = LocalDateTime.parse(dto.getRecruitmentEnd(), formatter);

			String[] parts = dto.getActivityTime().split(":");
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);
			LocalDateTime endAt = startAt.plusHours(hours).plusMinutes(minutes);

			String fileUrl = s3Service.uploadFile(dto.getImage());
			activityPostRepository.save(dto.toEntity(fileUrl, startAt, endAt, recruitmentEnd, activity));
		} catch (Exception e) {
			throw new ConflictException(ErrorCode.CONFLICT_ACTIVITY_POST);
		}
	}

	public ResponseEntity<List<ActivityPostResponseDTO>> readActivityPostList(
			int page, int pageSize, String searchRegion, String categories, String userEmail
	) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		try {
			List<Category> categoryList = (categories == null || categories.isBlank())
					? null : Arrays.stream(categories.split(",")).map(Category::valueOf).toList();
			PageRequest pageable = PageRequest.of(page -1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
			Page<ActivityPost> activityPosts = activityPostCustomRepository.findAllActivityPost(pageable, user.getPreferredRegion(), searchRegion, categoryList);
			List<ActivityPostResponseDTO> body = activityPosts.stream()
					.map(ActivityPostResponseDTO::fromEntity)
					.toList();
			return ResponseEntity.ok(body);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void updateActivityPost(Long id, ActivityPostRequestDTO dto, String userEmail) throws IOException {
		ActivityPost post = activityPostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));
		Institution institution = institutionRepository.findByEmail(userEmail)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
		Activity activity = activityRepository.findById(dto.getActivityId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));

		if (!activity.getInstitution().getId().equals(institution.getId())) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime startAt = LocalDateTime.parse(dto.getStartAt(), formatter);
			LocalDateTime recruitmentEnd = LocalDateTime.parse(dto.getRecruitmentEnd(), formatter);

			String[] parts = dto.getActivityTime().split(":");
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);
			LocalDateTime endAt = startAt.plusHours(hours).plusMinutes(minutes);

			String fileUrl = (dto.getImage() != null && !dto.getImage().isEmpty())
					? s3Service.uploadFile(dto.getImage())
					: post.getImageUrl();

			System.out.println(post);
			post = post.toBuilder()
					.activity(activity)
					.title(dto.getTitle())
					.content(dto.getContent())
					.startAt(startAt)
					.endAt(endAt)
					.recruitmentEnd(recruitmentEnd)
					.capacity(dto.getCapacity())
					.location(dto.getLocation())
					.imageUrl(fileUrl)
					.build();

			activityPostRepository.save(post);
		} catch (Exception e) {
			throw new ConflictException(ErrorCode.CONFLICT_ACTIVITY_POST);
		}
	}

	@Transactional
	public void deleteActivityPost(Long id, String userEmail) {
		Institution institution = institutionRepository.findByEmail(userEmail)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
		ActivityPost post = activityPostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));
		Activity activity = activityRepository.findById(post.getActivity().getId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));

		if (!activity.getInstitution().getId().equals(institution.getId())) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}

		post.markDeleted();
		activityPostRepository.save(post);
	}

	@Transactional
	public void closeActivityPost(Long id, String userEmail) {
		Institution institution = institutionRepository.findByEmail(userEmail)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
		ActivityPost post = activityPostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));
		Activity activity = activityRepository.findById(post.getActivity().getId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));

		if (!activity.getInstitution().getId().equals(institution.getId())) {
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}

		post.setRecruitmentEnd(LocalDateTime.now());
		activityPostRepository.save(post);
	}

	public ActivityPostDetailResponseDTO getPost(Long id) {
		ActivityPost post = activityPostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_POST));

		Activity activity = post.getActivity();

		List<ActivityReview> activityReviews = activityReviewRepository.findByActivity(activity);
		List<ActivityReviewResponseDTO> reviewDtos = new ArrayList<>();

		double totalAvgRate = 0;
		if (!activityReviews.isEmpty()) { // 리뷰가 있다면
			totalAvgRate = activityReviews.stream()
					.mapToInt(ActivityReview::getRate)
					.average().orElse(0);

			reviewDtos = activityReviews.stream()
					.map(ActivityPostService::reviewToDto)
					.toList();
		}

		// 모집 마감 날짜까지 남은 일수 계산
		LocalDate now = LocalDate.now();
		long dDay = ChronoUnit.DAYS.between(now, post.getRecruitmentEnd());
		String strDday;

		if (dDay > 0) {
			strDday = String.format("D-%d", dDay);
		} else if (dDay < 0) {
			strDday = "모집 마감";
		} else {
			strDday = "D-DAY";
		}

		return toDto(post).toBuilder()
				.dDay(strDday)
				.category(activity.getCategory().getDescription())
				.reviews(reviewDtos)
				.institutionName(activity.getInstitution().getName())
				.institutionPhoneNumber(activity.getInstitution().getPhoneNumber())
				.totalAvgScore(totalAvgRate)
				.build();
	}

	@Transactional
	public void applyActivity(String email, Long activityPostId) {
		ActivityPost post = activityPostRepository.findById(activityPostId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		Optional<Applicant> tmp = applicantRepository.findByUserAndActivityPost(user, post);

		if (tmp.isPresent() && tmp.get().getDeletedAt() == null) { // 이미 해당 봉사활동에 신청한 유저라면
			throw new BadRequestException(ErrorCode.BAD_REQUEST_ALREADY_APPLIED);
		}
		if (post.getRecruitmentEnd().isBefore(LocalDateTime.now())) { // 마감일이 지난 봉사 모집글이라면
			throw new BadRequestException(ErrorCode.BAD_REQUEST_RECRUITMENT_DATE_EXPIRED);
		}

		Applicant applicant = Applicant.builder()
				.activityPost(post)
				.user(user)
				.hours(BigDecimal.valueOf(ChronoUnit.HOURS.between(post.getStartAt(), post.getEndAt())))
				.status(ApprovalStatus.PENDING)
				.build();

		applicantRepository.save(applicant);
	}

	public void cancelActivity(String email, Long activityPostId) {
		ActivityPost post = activityPostRepository.findById(activityPostId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		Optional<Applicant> tmp = applicantRepository.findByUserAndActivityPost(user, post);

		if (tmp.isEmpty()) { // 이미 해당 봉사활동에 신청한 유저라면
			throw new NotFoundException(ErrorCode.NOTFOUND_APPLICANT);
		}

		Applicant applicant = tmp.get();

		if (!applicant.getStatus().equals(ApprovalStatus.PENDING)) { // 신청 상태가 대기중일 경우만 취소 가능
			throw new BadRequestException(ErrorCode.BAD_REQUEST_STATUS_NOT_PENDING);
		}

		// 삭제
		applicant.markDeleted();
		applicantRepository.save(applicant);
	}

	public List<ActivityPostResponseDTO> getMyActivityPosts(String email) {
		Institution institution = institutionRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		List<Activity> activities = activityRepository.findByInstitution(institution);

		List<ActivityPostResponseDTO> dtos = new ArrayList<>();
		activities.forEach(
				activity ->
				{
					List<ActivityPostResponseDTO> list = activity.getActivityPosts().stream()
							.map(post -> {
								// 지원자 총 인원 수
								long applicants = post.getApplicants().stream()
										.filter(applicant -> !applicant.getStatus().equals(ApprovalStatus.REJECTED))
										.count();

								return ActivityPostResponseDTO.builder()
										.id(post.getId())
										.title(post.getTitle())
										.endAt(String.format(
												"%d.%02d.%02d",
												post.getEndAt().getYear(),
												post.getEndAt().getMonthValue(),
												post.getEndAt().getDayOfMonth())
										)
										.location(post.getLocation())
										.imageUrl(post.getImageUrl())
										.category(post.getActivity().getCategory())
										.applicantNum((int) applicants)
										.build();
							}).toList();
					dtos.addAll(list);
				}
		);
		return dtos;
	}

	private static ActivityReviewResponseDTO reviewToDto(ActivityReview review) {
		return ActivityReviewResponseDTO.builder()
				.id(review.getId())
				.userName(review.getUser().getName())
				.profileImage(review.getUser().getProfileImage())
				.rate(review.getRate())
				.comment(review.getContent())
				.build();
	}

	private static ActivityPostDetailResponseDTO toDto(ActivityPost post) {

		LocalDateTime startAt = post.getStartAt();
		LocalDateTime endAt = post.getEndAt();

		return ActivityPostDetailResponseDTO.builder()
				.id(post.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.date(String.format("%04d.%02d.%02d", startAt.getYear(), startAt.getMonthValue(), startAt.getDayOfMonth()))
				.time(String.format("%02d:%02d - %02d:%02d", startAt.getHour(), startAt.getMinute(), endAt.getHour(), endAt.getMinute()))
				.capacity(post.getCapacity())
				.location(post.getLocation())
				.imageUrl(post.getImageUrl())
				.build();
	}


}
