package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityPostRepository extends JpaRepository<ActivityPost, Long> {
	List<ActivityPost> findByActivity_IdAndEndAtBefore(Long activityId, LocalDateTime endAtAfter);

	List<ActivityPost> findByActivity_IdAndRecruitmentEndAfter(Long activityId, LocalDateTime recruitmentEndAfter);
}
