package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityRequestDTO;
import com.hana7.ddabong.dto.ActivityResponseDTO;
import com.hana7.ddabong.dto.ActivityUpdateDTO;
import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityCustomRepository;
import com.hana7.ddabong.repository.ActivityRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public void createActivity(ActivityRequestDTO dto, String userEmail) {
        try {
            Institution institution = institutionRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("기관을 찾을 수 없습니다. email=" + userEmail));
            activityRepository.save(dto.toEntity(institution.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 내 봉사활동 리스트 확인(기관)
    public List<ActivityResponseDTO> readActivityList(
            int page,
            int pageSize,
            String keyWord,
            String userEmail
    ) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("기관을 찾을 수 없습니다. email=" + userEmail));
        try {
            PageRequest pageable = PageRequest.of(page -1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
            Page<Activity> result = activityCustomRepository.findActivityByInstitution(institution.getId(), pageable, keyWord);
            return result.stream()
                    .map(ActivityResponseDTO::fromEntity)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 내 봉사활동 개별 확인
    public ActivityResponseDTO readActivity(Long activityId, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("기관을 찾을 수 없습니다. email=" + userEmail));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("활동을 찾을 수 없습니다. id=" + activityId));

        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new IllegalStateException("해당 활동을 열람할 권한이 없습니다.");
        }

        try {
            return ActivityResponseDTO.fromEntity(activity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 내 봉사활동 수정
    public void updateActivity(ActivityUpdateDTO dto, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("기관을 찾을 수 없습니다. email=" + userEmail));

        Activity activity = activityRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("활동을 찾을 수 없습니다. id=" + dto.getId()));


        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new IllegalStateException("해당 활동을 수정할 권한이 없습니다.");
        }

        // 개별 수정 가능
        if (dto.getTitle() != null) {
            activity.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            activity.setContent(dto.getContent());
        }
        if (dto.getCategory() != null) {
            activity.setCategory(dto.getCategory());
        }

        activityRepository.save(activity);
    }
    // 내 봉사활동 삭제
    public void deleteActivity(Long activityId, String userEmail) {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("기관을 찾을 수 없습니다. email=" + userEmail));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("활동을 찾을 수 없습니다. id=" + activityId));

        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new IllegalStateException("해당 활동을 삭제할 권한이 없습니다.");
        }
        activity.markDeleted();
        activityRepository.save(activity);
    }
}
