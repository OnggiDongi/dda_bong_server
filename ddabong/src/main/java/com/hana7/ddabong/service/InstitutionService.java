package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.InstitutionRequestDTO;
import com.hana7.ddabong.dto.InstitutionResponseDTO;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.ConflictException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstitutionService {

	private final InstitutionRepository institutionRepository;

	@Transactional
	public void signUp(InstitutionRequestDTO institutionRequestDTO){
		Institution institution = institutionRepository.findByEmail(institutionRequestDTO.getEmail())
				.orElse(null);

		if(institution != null){ // 이미 존재하는 기관이라면 회원가입 방지
			throw new ConflictException(ErrorCode.CONFLICT_INSTITUTION);
		}

		Institution inst = institutionRequestDTO.toEntity();
		institutionRepository.save(inst);
	}

	public InstitutionResponseDTO getInstitutionInfo(Long id){
		Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		return toDto(institution);
	}

	private InstitutionResponseDTO toDto(Institution institution){
		return InstitutionResponseDTO.builder()
				.id(institution.getId())
				.name(institution.getName())
				.email(institution.getEmail())
				.phoneNumber(institution.getPhoneNumber())
				.detail(institution.getDetail())
				.build();
	}
}
