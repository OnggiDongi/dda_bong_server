package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityPostDetailResponseDTO;
import com.hana7.ddabong.dto.ActivityPostResponseDTO;
import com.hana7.ddabong.dto.ActivityReviewResponseDTO;
import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityPostRepository;
import com.hana7.ddabong.repository.ActivityReviewRepository;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityPostService {
	private final ActivityPostRepository activityPostRepository;
	private final ActivityReviewRepository activityReviewRepository;
	private final UserRepository userRepository;
	private final ApplicantRepository applicantRepository;

	public ActivityPostDetailResponseDTO getPost(Long id){
		ActivityPost post = activityPostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_POST));

		Activity activity = post.getActivity();

		List<ActivityReview> activityReviews = activityReviewRepository.findByActivity(activity);
		List<ActivityReviewResponseDTO> reviewDtos = new ArrayList<>();

		double totalAvgRate = 0;
		if(!activityReviews.isEmpty()){ // 리뷰가 있다면
			totalAvgRate = activityReviews.stream()
					.mapToInt(ActivityReview::getRate)
					.average().orElse(0);

			reviewDtos = activityReviews.stream()
					.map(ActivityPostService::reviewToDto)
					.toList();
		}

		// 모집 마감 날짜까지 남은 일수 계산
		LocalDate now =  LocalDate.now();
		long dDay = ChronoUnit.DAYS.between(now, post.getRecruitmentEnd());
		String strDday;

		if(dDay > 0){
			strDday = String.format("D-%d", dDay);
		} else if(dDay < 0){
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
	public void applyActivity(String email, Long activityPostId){
		ActivityPost post = activityPostRepository.findById(activityPostId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		Optional<Applicant> tmp = applicantRepository.findByUserAndActivityPost(user, post);

		if(tmp.isPresent()){ // 이미 해당 봉사활동에 신청한 유저라면
			throw new BadRequestException(ErrorCode.BAD_REQUEST_ALREADY_APPLIED);
		}
		if(post.getRecruitmentEnd().isBefore(LocalDateTime.now())){ // 마감일이 지난 봉사 모집글이라면
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

	private static ActivityReviewResponseDTO reviewToDto(ActivityReview review){
		return ActivityReviewResponseDTO.builder()
				.id(review.getId())
				.userName(review.getUser().getName())
				.profileImage(review.getUser().getProfileImage())
				.rate(review.getRate())
				.comment(review.getContent())
				.build();
	}

	private static ActivityPostDetailResponseDTO toDto(ActivityPost post){

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
