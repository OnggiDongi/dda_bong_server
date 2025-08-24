package com.hana7.ddabong.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.hana7.ddabong.enums.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="name", length = 20, nullable = false)
	private String name;

	@Column(name="email", length = 30, nullable = false)
	private String email;

	@Column(name = "password", length = 60, nullable = false)
	private String password;

	@Column(name = "phone_number", length = 13, nullable = false)
	private String phoneNumber;

	@Column(name = "total_hour", nullable = false)
	@ColumnDefault("0")
	private int totalHour;

	@Column(name="birthdate", nullable = false)
	private LocalDate birthdate;

	@Column(name = "is_kakao", nullable = false)
	private boolean isKakao;

	@Column(name = "preferred_region", length = 30)
	private String preferredRegion;

	@ElementCollection(targetClass = Category.class)
	@Enumerated(EnumType.STRING)
	@Column(name = "preferred_category")
	@Builder.Default
	private List<Category> preferredCategory = new ArrayList<>();

	@OneToMany(
		mappedBy = "user",
		cascade = CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<UserReview> userReviews = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@Builder.Default
	@ToString.Exclude
	private List<ActivityReview> activityReviews = new ArrayList<>();

	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<Likes> likes = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@Builder.Default
	@ToString.Exclude
	private List<Applicant> applicants = new ArrayList<>();

	// 인증서 - 일반 유저용
	@OneToMany(mappedBy = "user")
	@Builder.Default
	@ToString.Exclude
	private List<Certification> certifications = new ArrayList<>();

}
