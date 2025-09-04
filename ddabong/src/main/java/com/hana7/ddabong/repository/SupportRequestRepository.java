package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
	List<SupportRequest> findByActivityPost_Id(Long activityPostId);
}
