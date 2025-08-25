package com.hana7.ddabong.repository;

<<<<<<< HEAD
import java.util.Optional;

import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u.id from User u where u.email = :email")
	Optional<Long> findIdByEmail(@Param("email") String email);
=======
import com.hana7.ddabong.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
>>>>>>> 9c2f849b2a16ebf6f963ede911ea10c4bfe98f78
}
