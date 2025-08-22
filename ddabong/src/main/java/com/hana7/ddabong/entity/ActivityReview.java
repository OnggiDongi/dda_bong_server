package com.hana7.ddabong.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ActivityReview extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int rate;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "image_url", length = 128, nullable = false)
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "activity_id",
		foreignKey = @ForeignKey(name = "fk_ActivityReview_Activity",
			foreignKeyDefinition = """
					foreign key (activity_id)
					references Activity(id)
					on DELETE cascade on UPDATE cascade
				""")
	)
	private Activity activity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "user_id",
		foreignKey = @ForeignKey(name = "fk_ActivityReview_User",
			foreignKeyDefinition = """
					foreign key (user_id)
					references User(id)
					on DELETE set null on UPDATE cascade
				""")
	)
	private User user;
}
