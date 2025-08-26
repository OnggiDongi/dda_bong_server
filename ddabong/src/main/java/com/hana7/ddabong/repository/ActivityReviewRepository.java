package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityReviewRepository extends JpaRepository<ActivityReview, Long> {
    List<ActivityReview> findByUser_Email(String email);
}
