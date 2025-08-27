package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.ActivityPostRequestDTO;
import com.hana7.ddabong.entity.Activity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActivityPostService {

    private final ActivityPostRepository activityPostRepository;
    private final ActivityRepository activityRepository;
    private final InstitutionRepository institutionRepository;
    private final S3Service s3Service;
//    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d{1,2}):(\\d{2})$");

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

}
