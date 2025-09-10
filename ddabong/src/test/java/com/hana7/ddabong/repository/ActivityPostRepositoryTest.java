package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityPostRepositoryTest {

    @Autowired
    private ActivityPostRepository activityPostRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Long testActivityPostId;
    private static Long testActivityId;
    private static Long testInstitutionId;

    @Test
    @Order(1)
    void saveActivityPostTest() {

        Institution newInstitution = Institution.builder()
                .name("테스트 기관 포스트")
                .email("institution.post@example.com")
                .password(passwordEncoder.encode("inst-password"))
                .phoneNumber("02-1234-5679")
                .build();
        Institution savedInstitution = institutionRepository.save(newInstitution);
        testInstitutionId = savedInstitution.getId();

        Activity newActivity = Activity.builder()
                .title("테스트 봉사활동 포스트")
                .content("테스트 봉사활동 내용입니다.")
                .category(Category.EDUCATION)
                .institution(savedInstitution)
                .build();
        Activity savedActivity = activityRepository.save(newActivity);
        testActivityId = savedActivity.getId();

        //여기서부터 제대로 시작
        ActivityPost newActivityPost = ActivityPost.builder()
                .title("게시물 제목")
                .content("게시물 내용")
                .startAt(LocalDateTime.now().plusDays(1))
                .endAt(LocalDateTime.now().plusDays(2))
                .recruitmentEnd(LocalDateTime.now().plusHours(12))
                .location("온라인")
                .imageUrl("http://example.com/image.jpg")
                .activity(savedActivity)
                .build();

        ActivityPost savedActivityPost = activityPostRepository.save(newActivityPost);
        testActivityPostId = savedActivityPost.getId();

        assertThat(testActivityPostId).isNotNull();
        assertThat(savedActivityPost.getTitle()).isEqualTo("게시물 제목");
        assertThat(savedActivityPost.getActivity().getId()).isEqualTo(testActivityId);
    }

    @Test
    @Order(2)
    void readAndUpdateActivityPostTest() {

        Optional<ActivityPost> activityPostOptional = activityPostRepository.findById(testActivityPostId);
        assertThat(activityPostOptional).as("이전 테스트에서 게시물이 저장되어 있어야 합니다.").isPresent();
        ActivityPost activityPost = activityPostOptional.get();
        String updatedContent = "수정된 게시물 내용입니다.";

        activityPost.setContent(updatedContent);
        activityPostRepository.save(activityPost);

        Optional<ActivityPost> updatedActivityPostOptional = activityPostRepository.findById(testActivityPostId);
        assertThat(updatedActivityPostOptional).isPresent();
        assertThat(updatedActivityPostOptional.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @Order(3)
    void deleteActivityPostTest() {

        Optional<ActivityPost> activityPostOptional = activityPostRepository.findById(testActivityPostId);
        assertThat(activityPostOptional).as("수정 테스트에서 게시물이 남아있어야 합니다.").isPresent();


        // 자식
        activityPostRepository.delete(activityPostOptional.get());
        
        // 부모
        activityRepository.findById(testActivityId).ifPresent(activityRepository::delete);
        institutionRepository.findById(testInstitutionId).ifPresent(institutionRepository::delete);

        // 확인
        Optional<ActivityPost> deletedActivityPostOptional = activityPostRepository.findById(testActivityPostId);
        assertThat(deletedActivityPostOptional).isNotPresent();
    }
}