package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByUserId(Long userId);

    List<UserReview> findByUserIdIn(List<Long> userIds);

}
