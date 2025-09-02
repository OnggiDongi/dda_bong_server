package com.hana7.ddabong.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.ddabong.dto.InstitutionSummaryResponseDTO;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.InstitutionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstitutionSummaryService {

	private final InstitutionRepository institutionRepository;
	public InstitutionSummaryResponseDTO findInstitutionSummaryByEmail(String email) {
		Institution institution = institutionRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		return InstitutionSummaryResponseDTO.builder()
			.name(institution.getName())
			.build();
	}

}
