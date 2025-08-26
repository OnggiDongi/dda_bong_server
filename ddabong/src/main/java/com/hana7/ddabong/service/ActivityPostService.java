package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityPostRequestDTO;
import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityPostRepository;
import com.hana7.ddabong.repository.ActivityRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActivityPostService {

    private final ActivityPostRepository activityPostRepository;
    private final ActivityRepository activityRepository;
    private final InstitutionRepository institutionRepository;
    private final S3Service s3Service;

    @Transactional
    public void createActivityPost(ActivityPostRequestDTO dto, String userEmail) throws IOException {
        Institution institution = institutionRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));
        Activity activity = activityRepository.findById(dto.getActivity().getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY));
        if (!activity.getInstitution().getId().equals(institution.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
        }
        try {
            String fileUrl = s3Service.uploadFile(dto.getImage());
            activityPostRepository.save(dto.toEntity(fileUrl));
        } catch (Exception e) {
            throw new ConflictException(ErrorCode.CONFLICT_ACTIVITY_POST);
        }
    }

}
