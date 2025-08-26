package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityCustomRepository {
    Page<Activity> findActivityByInstitution(Long institutionId, Pageable pageable, String keyWord);
}
