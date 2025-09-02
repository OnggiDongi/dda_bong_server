package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityRequestDTO;
import com.hana7.ddabong.dto.ActivityResponseDTO;
import com.hana7.ddabong.dto.ActivityUpdateDTO;
import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityCustomRepository;
import com.hana7.ddabong.repository.ActivityRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityCustomRepository activityCustomRepository;
    private final InstitutionRepository institutionRepository;

    // 봉사활동 생성
    @Transactional
    public void createActivity(ActivityRequestDTO dto, String userEmail) {
        try {
            Institution institution = institutionRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
            activityRepository.save(dto.toEntity(institution.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 내 봉사활동 리스트 확인(기관)
    public ResponseEntity<List<ActivityResponseDTO>> readActivityList(
            int page,
            int pageSize,
            String keyWord,
            String userEmail
    ) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
        try {
            PageRequest pageable = PageRequest.of(page -1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
            Page<Activity> activities = activityCustomRepository.findActivityByInstitution(institution.getId(), pageable, keyWord);
            List<ActivityResponseDTO> body = activities.stream()
                    .map(ActivityResponseDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 내 봉사활동 개별 확인
    public ResponseEntity<ActivityResponseDTO> readActivity(Long activityId, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
        Activity activity = activityRepository.findByIdAndDeletedAtIsNull(activityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));

        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
        }

        try {
            ActivityResponseDTO body = ActivityResponseDTO.fromEntity(activity);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 내 봉사활동 수정
    @Transactional
    public void updateActivity(ActivityUpdateDTO dto, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

        Activity activity = activityRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));


        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
        }

        // 개별 수정 가능
        activity = activity.toBuilder()
                .title(dto.getTitle() != null ? dto.getTitle() : activity.getTitle())
                .content(dto.getContent() != null ? dto.getContent() : activity.getContent())
                .category(dto.getCategory() != null ? dto.getCategory() : activity.getCategory())
                .build();

        activityRepository.save(activity);
    }
    // 내 봉사활동 삭제
    @Transactional
    public void deleteActivity(Long activityId, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));

        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
        }
        activity.markDeleted();
        activityRepository.save(activity);
    }
}
