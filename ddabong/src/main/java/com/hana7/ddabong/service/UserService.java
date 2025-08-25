package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.UserOnboardingRequestDTO;
import com.hana7.ddabong.dto.UserResponseDTO;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + id + "인 회원이 없습니다"));
        return toDTO(user);
    }

    @Transactional
    public UserResponseDTO updateOnboardingInfo(Long id, UserOnboardingRequestDTO userOnboardingRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + id + "인 회원이 없습니다"));

        user = user.toBuilder()
                .preferredRegion(userOnboardingRequestDTO.getPreferredRegion())
                .preferredCategory(userOnboardingRequestDTO.getPreferredCategory())
                .build();

        userRepository.save(user);

        return toDTO(user);
    }


    private UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalHour(user.getTotalHour())
                .birthdate(user.getBirthdate())
                .isKakao(user.isKakao())
                .preferredRegion(user.getPreferredRegion())
                .profileImage(user.getProfileImage())
                .preferredCategory(user.getPreferredCategory().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .build();
    }
}
