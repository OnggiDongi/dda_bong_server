package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.*;
import com.hana7.ddabong.enums.ApprovalStatus;
import com.hana7.ddabong.enums.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicantRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;
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

    private static Long testApplicantId;
    private static Long testUserId;
    private static Long testActivityPostId;
    private static Long testActivityId;
    private static Long testInstitutionId;

    @Test
    @Order(1)
    void saveApplicantTest() {
        // 지원자를저장하기위해 생성한는 것들
        Institution institution = institutionRepository.save(Institution.builder()
                .name("지원자 테스트 기관").email("applicant.inst@example.com")
                .password(passwordEncoder.encode("pw")).phoneNumber("02-111-1111").build());
        testInstitutionId = institution.getId();

        Activity activity = activityRepository.save(Activity.builder()
                .title("지원자 테스트 활동").content("내용").category(Category.SAFETY).institution(institution).build());
        testActivityId = activity.getId();

        ActivityPost activityPost = activityPostRepository.save(ActivityPost.builder()
                .title("지원자 테스트 게시물").content("내용").startAt(LocalDateTime.now()).endAt(LocalDateTime.now().plusHours(2))
                .recruitmentEnd(LocalDateTime.now().plusDays(1)).location("서울").imageUrl("url").activity(activity).build());
        testActivityPostId = activityPost.getId();

        User user = userRepository.save(User.builder()
                .name("지원자").email("applicant.user@example.com").password(passwordEncoder.encode("pw"))
                .phoneNumber("010-222-2222").birthdate(LocalDate.now().minusYears(25)).build());
        testUserId = user.getId();

        // 여기부터 메인
        Applicant newApplicant = Applicant.builder()
                .hours(new BigDecimal("8.00"))
                .status(ApprovalStatus.PENDING)
                .user(user)
                .activityPost(activityPost)
                .build();

        Applicant savedApplicant = applicantRepository.save(newApplicant);
        testApplicantId = savedApplicant.getId();

        assertThat(testApplicantId).isNotNull();
        assertThat(savedApplicant.getStatus()).isEqualTo(ApprovalStatus.PENDING);
        assertThat(savedApplicant.getUser().getName()).isEqualTo("지원자");
    }

    @Test
    @Order(2)
    void readAndUpdateApplicantTest() {

        Optional<Applicant> applicantOptional = applicantRepository.findById(testApplicantId);
        assertThat(applicantOptional).as("이전 테스트에서 지원자가 저장되어 있어야 합니다.").isPresent();
        Applicant applicant = applicantOptional.get();

        applicant.setStatus(ApprovalStatus.APPROVED);
        applicantRepository.save(applicant);

        Optional<Applicant> updatedApplicantOptional = applicantRepository.findById(testApplicantId);
        assertThat(updatedApplicantOptional).isPresent();
        assertThat(updatedApplicantOptional.get().getStatus()).isEqualTo(ApprovalStatus.APPROVED);
    }

    @Test
    @Order(3)
    void deleteApplicantTest() {

        Optional<Applicant> applicantOptional = applicantRepository.findById(testApplicantId);
        assertThat(applicantOptional).as("수정 테스트에서 지원자가 남아있어야 합니다.").isPresent();

        userRepository.deleteById(testUserId);
        activityPostRepository.deleteById(testActivityPostId);
        activityRepository.deleteById(testActivityId);
        institutionRepository.deleteById(testInstitutionId);

        Optional<Applicant> deletedApplicantOptional = applicantRepository.findById(testApplicantId);
        assertThat(deletedApplicantOptional).isNotPresent();
    }
}
