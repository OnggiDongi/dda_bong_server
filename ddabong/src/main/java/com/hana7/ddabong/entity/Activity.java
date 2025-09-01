package com.hana7.ddabong.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hana7.ddabong.enums.Category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Activity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(length = 20) // ENUM 문자열 길이 여유롭게
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "institution",
		foreignKey = @ForeignKey(
			name = "fk_Activity_Institution",
			foreignKeyDefinition = """
					foreign key (institution)
					   references Institution(id)
					    on DELETE set null on UPDATE cascade
				"""
		)
	)
	private Institution institution;

	@OneToMany(
			mappedBy = "activity",
			cascade = CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<ActivityPost> activityPosts = new ArrayList<>();

	@OneToMany(mappedBy = "activity")
	@Builder.Default
	@ToString.Exclude
	private List<ActivityReview> activityReviews = new ArrayList<>();
}
