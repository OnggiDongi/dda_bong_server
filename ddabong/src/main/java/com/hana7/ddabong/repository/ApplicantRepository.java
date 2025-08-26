package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Applicant;
import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByUser(User user);

    boolean existsByUserAndActivityPostId(User user, Long activityPostId);
}
