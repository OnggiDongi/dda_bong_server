package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.enums.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Long testActivityId;
    private static Long testInstitutionId;

    @Test
    @Order(1)
    void saveActivityTest() {
        // 기관 만들어주고 봉사활동
        Institution newInstitution = Institution.builder()
                .name("테스트 기관")
                .email("institution@example.com")
                .password(passwordEncoder.encode("inst-password"))
                .phoneNumber("02-1234-5678")
                .build();
        Institution savedInstitution = institutionRepository.save(newInstitution);
        testInstitutionId = savedInstitution.getId();

        Activity newActivity = Activity.builder()
                .title("테스트 봉사활동")
                .content("테스트 봉사활동 내용입니다.")
                .category(Category.EDUCATION)
                .institution(savedInstitution)
                .build();


        Activity savedActivity = activityRepository.save(newActivity);
        testActivityId = savedActivity.getId();

        assertThat(testActivityId).isNotNull();
        assertThat(savedActivity.getTitle()).isEqualTo("테스트 봉사활동");
        assertThat(savedActivity.getInstitution().getName()).isEqualTo("테스트 기관");
    }

    @Test
    @Order(2)
    void readAndUpdateActivityTest() {

        Optional<Activity> activityOptional = activityRepository.findById(testActivityId);
        assertThat(activityOptional).as("이전 테스트에서 봉사활동이 저장되어 있어야 합니다.").isPresent();
        Activity activity = activityOptional.get();
        String updatedTitle = "수정된 봉사활동 제목";

        activity.setTitle(updatedTitle);
        activityRepository.save(activity);

        Optional<Activity> updatedActivityOptional = activityRepository.findById(testActivityId);
        assertThat(updatedActivityOptional).isPresent();
        assertThat(updatedActivityOptional.get().getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @Order(3)
    void deleteActivityTest() {

        Optional<Activity> activityOptional = activityRepository.findById(testActivityId);
        assertThat(activityOptional).as("수정 테스트에서 봉사활동이 남아있어야 합니다.").isPresent();

        Optional<Institution> institutionOptional = institutionRepository.findById(testInstitutionId);
        assertThat(institutionOptional).as("수정 테스트에서 기관이 남아있어야 합니다.").isPresent();

        activityRepository.delete(activityOptional.get());
        institutionRepository.delete(institutionOptional.get());

        Optional<Activity> deletedActivityOptional = activityRepository.findById(testActivityId);
        assertThat(deletedActivityOptional).isNotPresent();

        Optional<Institution> deletedInstitutionOptional = institutionRepository.findById(testInstitutionId);
        assertThat(deletedInstitutionOptional).isNotPresent();
    }
}