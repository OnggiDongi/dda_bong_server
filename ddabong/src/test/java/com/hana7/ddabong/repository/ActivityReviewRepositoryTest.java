package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityReviewRepositoryTest {

    @Autowired
    private ActivityReviewRepository activityReviewRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Long testReviewId;
    private static Long testActivityId;
    private static Long testInstitutionId;
    private static Long testUserId;

    @Test
    @Order(1)
    void saveActivityReviewTest() {

        Institution newInstitution = Institution.builder()
                .name("리뷰용 기관")
                .email("institution.review@example.com")
                .password(passwordEncoder.encode("inst-password"))
                .phoneNumber("02-1234-5611")
                .build();
        Institution savedInstitution = institutionRepository.save(newInstitution);
        testInstitutionId = savedInstitution.getId();

        Activity newActivity = Activity.builder()
                .title("리뷰용 봉사활동")
                .content("리뷰용 봉사활동 내용입니다.")
                .category(Category.CULTURE)
                .institution(savedInstitution)
                .build();
        Activity savedActivity = activityRepository.save(newActivity);
        testActivityId = savedActivity.getId();

        User newUser = User.builder()
                .name("리뷰작성자")
                .email("reviewer@example.com")
                .password(passwordEncoder.encode("reviewer-password"))
                .phoneNumber("010-1111-2222")
                .birthdate(LocalDate.now().minusYears(20))
                .build();
        User savedUser = userRepository.save(newUser);
        testUserId = savedUser.getId();

        ActivityReview newReview = ActivityReview.builder()
                .rate(5)
                .content("정말 좋은 경험이었습니다.")
                .activity(savedActivity)
                .user(savedUser)
                .build();


        ActivityReview savedReview = activityReviewRepository.save(newReview);
        testReviewId = savedReview.getId();

        assertThat(testReviewId).isNotNull();
        assertThat(savedReview.getRate()).isEqualTo(5);
        assertThat(savedReview.getUser().getName()).isEqualTo("리뷰작성자");
    }

    @Test
    @Order(2)
    void readAndUpdateActivityReviewTest() {

        Optional<ActivityReview> reviewOptional = activityReviewRepository.findById(testReviewId);
        assertThat(reviewOptional).as("이전 테스트에서 리뷰가 저장되어 있어야 합니다.").isPresent();
        ActivityReview review = reviewOptional.get();
        String updatedContent = "수정된 리뷰 내용입니다. 최고예요!";

        review.setContent(updatedContent);
        activityReviewRepository.save(review);

        Optional<ActivityReview> updatedReviewOptional = activityReviewRepository.findById(testReviewId);
        assertThat(updatedReviewOptional).isPresent();
        assertThat(updatedReviewOptional.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @Order(3)
    void deleteActivityReviewTest() {

        Optional<ActivityReview> reviewOptional = activityReviewRepository.findById(testReviewId);
        assertThat(reviewOptional).as("수정 테스트에서 리뷰가 남아있어야 합니다.").isPresent();


        activityReviewRepository.delete(reviewOptional.get());

        activityRepository.findById(testActivityId).ifPresent(activityRepository::delete);
        institutionRepository.findById(testInstitutionId).ifPresent(institutionRepository::delete);
        userRepository.findById(testUserId).ifPresent(userRepository::delete);

        Optional<ActivityReview> deletedReviewOptional = activityReviewRepository.findById(testReviewId);
        assertThat(deletedReviewOptional).isNotPresent();
    }
}
