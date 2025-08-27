package com.hana7.ddabong.service;

import com.hana7.ddabong.entity.Applicant;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.BadRequestException;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ApplicantRepository;
import com.hana7.ddabong.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final InstitutionRepository	institutionRepository;

	@Transactional
	public void rejectApplicant(String email, Long applicantId) {
		Institution institution = institutionRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION));

		Applicant applicant = applicantRepository.findById(applicantId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_APPLICANT));


		// 삭제된 기관일 경우
		if (institution.getDeletedAt() != null){
			throw new NotFoundException(ErrorCode.NOTFOUND_INSTITUTION);
		}
		// 신청 취소한 지원자일 경우
		if (applicant.getDeletedAt() != null){
			throw new NotFoundException(ErrorCode.NOTFOUND_APPLICANT);
		}
		// 지원한 봉사활동을 등록한 기관이 아닐 경우
		if(!applicant.getActivityPost().getActivity().getInstitution().equals(institution)){
			throw new BadRequestException(ErrorCode.BAD_REQUEST_UNAUTHORIZED);
		}
		// 이미 승인 또는 거절한 지원자일 경우
		if (!applicant.getStatus().equals(ApprovalStatus.PENDING)){
			throw new BadRequestException(ErrorCode.BAD_REQUEST_ALREADY_PROCESSED_APPLICANT);
		}

		applicant = applicant.toBuilder()
				.status(ApprovalStatus.REJECTED)
				.build();
		applicantRepository.save(applicant);
	}
}
