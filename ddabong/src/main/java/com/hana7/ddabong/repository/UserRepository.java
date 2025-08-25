package com.hana7.ddabong.repository;

import java.util.Optional;

import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u.id from User u where u.email = :email")
	Optional<Long> findIdByEmail(@Param("email") String email);
}
