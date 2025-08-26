package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.ActivityReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityReviewRepository extends JpaRepository<ActivityReview, Long> {
	List<ActivityReview> findByActivity(Activity activity);
}
