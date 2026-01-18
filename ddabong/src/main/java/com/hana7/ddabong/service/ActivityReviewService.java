package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityMyReviewResponseDTO;
import com.hana7.ddabong.dto.ActivityReviewRequestDTO;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.ActivityReview;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityPostRepository;
import com.hana7.ddabong.repository.ActivityReviewRepository;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityReviewService {

    private final ActivityReviewRepository activityReviewRepository;
    private final UserRepository userRepository;
    private final ActivityPostRepository activityPostRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional
    public void createActivityReview(Long activityPostId, String email, ActivityReviewRequestDTO requestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        ActivityPost activityPost = activityPostRepository.findById(activityPostId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

        boolean isApprovedApplicant = applicantRepository.findByUserAndActivityPostAndDeletedAtIsNull(user, activityPost)
                .map(applicant -> applicant.getStatus() == ApprovalStatus.APPROVED)
                .orElse(false);
        if (!isApprovedApplicant) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_NOT_APPLICANT);
        }
        // TODO :: 실제 시간과 비교하여 활동 끝났는지 확인하려면 주석 풀기. 지금은 가라데이터라 여기서 걸려서 주석처리합니다리
         if (activityPost.getEndAt().isAfter(LocalDateTime.now())) {
             throw new BadRequestException(ErrorCode.BAD_REQUEST_ACTIVITY_NOT_COMPLETED);
         }

        ActivityReview activityReview = ActivityReview.builder()
                .rate(requestDTO.getRate())
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl() != null && !requestDTO.getImageUrl().isEmpty() ? requestDTO.getImageUrl() : "https://ddabong-upload.s3.ap-northeast-2.amazonaws.com/uploads/abbbe69a-308d-4b60-9874-7b5935046c7d-(Frame%202087327065.png)")
                .activity(activityPost.getActivity())
                .user(user)
                .build();

        activityReviewRepository.save(activityReview);
    }

    public List<ActivityMyReviewResponseDTO> getMyActivityReviews(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException(ErrorCode.NOTFOUND_USER);
        }
        return activityReviewRepository.findByUser_EmailAndDeletedAtIsNull(email).stream()
                .map(review -> ActivityMyReviewResponseDTO.toDTO(review, review.getActivity().getCategory().getDescription()))
                .collect(Collectors.toList());
    }

    public List<ActivityMyReviewResponseDTO> getActivityPostReviews(Long activityPostId) {
        ActivityPost activityPost = activityPostRepository.findById(activityPostId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

        Long activityId = activityPost.getActivity().getId();
        return activityReviewRepository.findByActivity_Id(activityId).stream()
                .map(review -> ActivityMyReviewResponseDTO.toDTO(review, review.getActivity().getCategory().getDescription()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteActivityReview(String email, Long activityReviewId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        ActivityReview activityReview = activityReviewRepository.findById(activityReviewId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_REVIEW));

        if(activityReview.getDeletedAt() != null){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_ALREADY_DELETED);
        }

        if(!activityReview.getUser().getId().equals(user.getId())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_NO_PERMISSION);
        }

        activityReview.markDeleted();
        activityReviewRepository.save(activityReview);
    }
}
