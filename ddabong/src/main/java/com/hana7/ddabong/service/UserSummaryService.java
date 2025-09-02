package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.UserSummaryResponseDTO;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSummaryService {

    private final UserRepository userRepository;

    public UserSummaryResponseDTO findUserSummaryByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

        String grade = user.getTotalHour() < 111 ? "SILVER" : "VIP";

        return UserSummaryResponseDTO.builder()
                .name(user.getName())
                .grade(grade)
                .totalHour(user.getTotalHour())
                .build();
    }
}
