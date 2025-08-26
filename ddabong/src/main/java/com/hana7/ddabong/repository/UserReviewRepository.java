package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByUserId(Long userId);
}
