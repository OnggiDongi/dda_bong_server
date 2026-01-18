package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByIdAndDeletedAtIsNull(Long id);

	List<Activity> findByInstitution(Institution institution);
}
