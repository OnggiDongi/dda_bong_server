package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.Category;
import com.hana7.ddabong.enums.SupportRequestType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SupportRequestRepositoryTest {

    @Autowired
    private SupportRequestRepository supportRequestRepository;
    @Autowired
    private ActivityPostRepository activityPostRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Long testSupportRequestId;
    private static Long testActivityPostId;
    private static Long testActivityId;
    private static Long testInstitutionId;

    @Test
    @Order(1)
    void saveSupportRequestTest() {
        // 기관
        Institution institution = institutionRepository.save(Institution.builder()
                .name("지원요청 테스트 기관").email("support.inst@example.com")
                .password(passwordEncoder.encode("pw1234#@")).phoneNumber("02-444-4444").build());
        testInstitutionId = institution.getId();
        // 활동
        Activity activity = activityRepository.save(Activity.builder()
                .title("지원요청 테스트 활동").content("내용").category(Category.PUBLIC).institution(institution).build());
        testActivityId = activity.getId();
        // 게시물
        ActivityPost activityPost = activityPostRepository.save(ActivityPost.builder()
                .title("지원요청 테스트 게시물").content("내용").startAt(LocalDateTime.now()).endAt(LocalDateTime.now().plusHours(2))
                .recruitmentEnd(LocalDateTime.now().plusDays(1)).location("제주").imageUrl("url").activity(activity).build());
        testActivityPostId = activityPost.getId();

        SupportRequest newSupportRequest = SupportRequest.builder()
                .supply(SupportRequestType.SNACK)
                .status(ApprovalStatus.PENDING)
                .activityPost(activityPost)
                .build();

        SupportRequest savedSupportRequest = supportRequestRepository.save(newSupportRequest);
        testSupportRequestId = savedSupportRequest.getId();

        assertThat(testSupportRequestId).isNotNull();
        assertThat(savedSupportRequest.getSupply()).isEqualTo(SupportRequestType.SNACK);
        assertThat(savedSupportRequest.getStatus()).isEqualTo(ApprovalStatus.PENDING);
    }

    @Test
    @Order(2)
    void readAndUpdateSupportRequestTest() {

        Optional<SupportRequest> supportRequestOptional = supportRequestRepository.findById(testSupportRequestId);
        assertThat(supportRequestOptional).as("이전 테스트에서 지원 요청이 저장되어 있어야 합니다.").isPresent();
        SupportRequest supportRequest = supportRequestOptional.get();

        supportRequest.setStatus(ApprovalStatus.APPROVED);
        supportRequestRepository.save(supportRequest);

        Optional<SupportRequest> updatedSupportRequestOptional = supportRequestRepository.findById(testSupportRequestId);
        assertThat(updatedSupportRequestOptional).isPresent();
        assertThat(updatedSupportRequestOptional.get().getStatus()).isEqualTo(ApprovalStatus.APPROVED);
    }

    @Test
    @Order(3)
    void deleteSupportRequestTest() {

        Optional<SupportRequest> supportRequestOptional = supportRequestRepository.findById(testSupportRequestId);
        assertThat(supportRequestOptional).as("수정 테스트에서 지원 요청이 남아있어야 합니다.").isPresent();

        supportRequestRepository.delete(supportRequestOptional.get());

        activityPostRepository.deleteById(testActivityPostId);
        activityRepository.deleteById(testActivityId);
        institutionRepository.deleteById(testInstitutionId);

        Optional<SupportRequest> deletedSupportRequestOptional = supportRequestRepository.findById(testSupportRequestId);
        assertThat(deletedSupportRequestOptional).isNotPresent();
    }
}
