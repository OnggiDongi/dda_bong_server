package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

