package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findActivityById(Long id);
}