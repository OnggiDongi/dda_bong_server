package com.hana7.ddabong.service;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.Likes;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.ActivityPostRepository;
import com.hana7.ddabong.repository.ActivityRepository;
import com.hana7.ddabong.repository.LikesRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

	private final ActivityPostRepository activityPostRepository;
	private final LikesRepository likesRepository;
	private final UserRepository userRepository;


	public void likeActivityPost(String email, Long activityPostId){
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_USER));

		ActivityPost activityPost = activityPostRepository.findById(activityPostId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOTFOUND_ACTIVITY_POST));

		Optional<Likes> like = likesRepository.findByUserAndActivityPost(user, activityPost);

		Likes save;
		if(like.isPresent() && like.get().getDeletedAt() != null){ // 찜하기 취소인 경우
			save = like.get().toBuilder()
					.deletedAt(null)
					.build();
		} else if (like.isPresent() && like.get().getDeletedAt() == null){ // 이미 찜하기를 누른 경우
			save = like.get();
			save.markDeleted();
		} else { // 해당 모집글에 처음으로 찜하기를 누른 경우
			save = Likes.builder()
					.activityPost(activityPost)
					.user(user)
					.build();
		}

		likesRepository.save(save);
	}
}
