package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
	Optional<Institution> findByEmail(String username);
}
