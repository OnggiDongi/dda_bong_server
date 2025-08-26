package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPostRepository extends JpaRepository<ActivityPost, Long> {
}
