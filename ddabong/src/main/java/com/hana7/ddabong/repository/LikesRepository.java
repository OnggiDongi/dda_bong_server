package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Likes;
import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUser(User user);
}
