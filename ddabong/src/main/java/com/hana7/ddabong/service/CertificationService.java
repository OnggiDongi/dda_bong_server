package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.CertificationResponseDTO;
import com.hana7.ddabong.entity.Certification;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.CertificationRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;

    public List<CertificationResponseDTO> getCertifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        List<Certification> certifications = certificationRepository.findAllByUser(user);
        return certifications.stream()
                .map(CertificationResponseDTO::from)
                .collect(Collectors.toList());
    }

    public CertificationResponseDTO getCertification(Long certificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_CERTIFICATION));

        if (!certification.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_CERTIFICATION_ACCESS_DENIED);
        }

        return CertificationResponseDTO.from(certification);
    }
}
