package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;

import com.hana7.ddabong.entity.ActivityReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ActivityReviewRepository extends JpaRepository<ActivityReview, Long> {
	List<ActivityReview> findByActivity(Activity activity);
    List<ActivityReview> findByUser_EmailAndDeletedAtIsNull(String email);
    List<ActivityReview> findByActivity_Id(Long activityId);
}
