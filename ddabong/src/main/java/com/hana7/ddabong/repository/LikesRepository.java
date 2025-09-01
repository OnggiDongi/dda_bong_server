package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.Likes;
import com.hana7.ddabong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	Optional<Likes> findByUserAndActivityPost(User user, ActivityPost activityPost);

	List<Likes> findByUserAndDeletedAtIsNull(User user);
}
