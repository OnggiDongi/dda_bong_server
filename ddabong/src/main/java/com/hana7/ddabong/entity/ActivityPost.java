package com.hana7.ddabong.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ActivityPost extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Comment("활동 시작일시")
	@Column(name = "start_at", nullable = false)
	private LocalDateTime startAt;

	@Comment("활동 종료일시")
	@Column(name = "end_at", nullable = false)
	private LocalDateTime endAt;

	@Comment("모집 시작일시")
	@Column(name = "recruitment_start", nullable = false)
	private LocalDateTime recruitmentStart;

	@Comment("모집 종료일시")
	@Column(name = "recruitment_end", nullable = false)
	private LocalDateTime recruitmentEnd;

	@Column(name = "is_accept", nullable = false)
	@Builder.Default
	@ColumnDefault("0")
	private boolean isAccept = false;

	@Column(nullable = false)
	@Builder.Default
	@ColumnDefault("1")
	private int capacity = 1;

	@Column(nullable = false, length = 128)
	private String location;

	@Column(name = "image_url", length = 128, nullable = false)
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "activity_id",
		foreignKey = @ForeignKey(name = "fk_ActivityPost_Activity")
	)
	private Activity activity;

	@OneToMany(
		mappedBy = "activityPost",
		cascade =  CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<SupportRequest> supportRequests = new ArrayList<>();

	@OneToMany(
		mappedBy = "activityPost",
		cascade =  CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<Likes> likes = new ArrayList<>();

	@OneToMany(
		mappedBy = "activityPost",
		cascade =  CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<Applicant>	applicants = new ArrayList<>();
}
