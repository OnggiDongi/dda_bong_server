package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityPostRepository activityPostRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Long testLikesId;
    private static User testUser;
    private static ActivityPost testActivityPost;
    private static Long testInstitutionId; // For cleanup
    private static Long testActivityId; // For cleanup

    @Test
    @Order(1)
    void saveLikesTest() {

        //기관만들고
        Institution institution = institutionRepository.save(Institution.builder()
                .name("좋아요 테스트 기관").email("likes.inst@example.com")
                .password(passwordEncoder.encode("pw")).phoneNumber("02-333-3333").build());
        testInstitutionId = institution.getId();

        //활동 만들고
        Activity activity = activityRepository.save(Activity.builder()
                .title("좋아요 테스트 활동").content("내용").category(Category.ENVIRONMENT).institution(institution).build());
        testActivityId = activity.getId();

        // 게시물 만들고
        testActivityPost = activityPostRepository.save(ActivityPost.builder()
                .title("좋아요 테스트 게시물").content("내용").startAt(LocalDateTime.now()).endAt(LocalDateTime.now().plusHours(2))
                .recruitmentEnd(LocalDateTime.now().plusDays(1)).location("부산").imageUrl("url").activity(activity).build());

        testUser = userRepository.save(User.builder()
                .name("좋아요 누른 유저").email("likes.user@example.com").password(passwordEncoder.encode("pwd1234!!"))
                .phoneNumber("010-333-3333").birthdate(LocalDate.now().minusYears(30)).build());

        Likes newLikes = Likes.builder()
                .user(testUser)
                .activityPost(testActivityPost)
                .build();

        Likes savedLikes = likesRepository.save(newLikes);
        testLikesId = savedLikes.getId();

        assertThat(testLikesId).isNotNull();
        assertThat(savedLikes.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(savedLikes.getActivityPost().getId()).isEqualTo(testActivityPost.getId());
    }

    @Test
    @Order(2)
    void readLikesTest() {

        Optional<Likes> likesOptional = likesRepository.findByUserAndActivityPost(testUser, testActivityPost);

        assertThat(likesOptional).as("이전 테스트에서 '좋아요'가 저장되어 있어야 합니다.").isPresent();
        assertThat(likesOptional.get().getId()).isEqualTo(testLikesId);
    }

    @Test
    @Order(3)
    void deleteLikesTest() {

        Optional<Likes> likesOptional = likesRepository.findById(testLikesId);
        assertThat(likesOptional).as("조회 테스트에서 '좋아요'가 남아있어야 합니다.").isPresent();

        likesRepository.delete(likesOptional.get());

        userRepository.deleteById(testUser.getId());
        activityPostRepository.deleteById(testActivityPost.getId());
        activityRepository.deleteById(testActivityId);
        institutionRepository.deleteById(testInstitutionId);

        Optional<Likes> deletedLikesOptional = likesRepository.findById(testLikesId);
        assertThat(deletedLikesOptional).isNotPresent();
    }
}
