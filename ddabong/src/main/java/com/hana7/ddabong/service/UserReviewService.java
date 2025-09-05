package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.UserReviewRequestDTO;
import com.hana7.ddabong.dto.UserReviewResponseDTO;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.entity.UserReview;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final ApplicantRepository applicantRepository;
    private final ActivityPostRepository activityPostRepository;

    @Transactional
    public void createUserReview(Long userId, UserReviewRequestDTO requestDTO) {
        Institution loggedInInstitution = getLoggedInInstitution();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        ActivityPost activityPost = activityPostRepository.findById(requestDTO.getActivityPostId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

        if (activityPost.getActivity().getInstitution() == null || !Objects.equals(activityPost.getActivity().getInstitution().getId(), loggedInInstitution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_INSTITUTION_MISMATCH);
        }

        boolean isApplicant = applicantRepository.existsByUserAndActivityPostId(user, requestDTO.getActivityPostId());
        if (!isApplicant) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_NOT_APPLICANT);
        }

        UserReview userReview = UserReview.builder()
                .user(user)
                .healthStatus(requestDTO.getHealthStatus())
                .diligenceLevel(requestDTO.getDiligenceLevel())
                .attitude(requestDTO.getAttitude())
                .memo(requestDTO.getMemo())
                .writeInst(loggedInInstitution.getId())
                .activityPost(activityPost)
                .build();
        userReviewRepository.save(userReview);
    }

    public List<UserReviewResponseDTO> getUserReviews(Long userId) {
        getLoggedInInstitution(); //기관만 접근 가능.
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(ErrorCode.NOTFOUND_USER);
        }
        return userReviewRepository.findByUserId(userId).stream()
                .map(UserReviewResponseDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserReview(Long reviewId) {
        Institution loggedInInstitution = getLoggedInInstitution();
        UserReview userReview = userReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_REVIEW));

        if (!Objects.equals(userReview.getWriteInst(), loggedInInstitution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_NO_PERMISSION);
        }
        userReviewRepository.deleteById(reviewId);
    }

    private Institution getLoggedInInstitution() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return institutionRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
    }
}
