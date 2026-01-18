package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.ddabong.entity.Certification;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findAllByUser(User user);

	boolean existsByUserAndHour(User user, int hour);
}
