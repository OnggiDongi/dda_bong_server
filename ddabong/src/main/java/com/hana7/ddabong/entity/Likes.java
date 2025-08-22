package com.hana7.ddabong.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Likes extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "user_id",
		foreignKey = @ForeignKey(
			name = "fk_Likes_User",
			foreignKeyDefinition = """
					foreign key (user_id)
					   references User(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	@ToString.Exclude
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "activity_post_id",
		foreignKey = @ForeignKey(
			name = "fk_Likes_ActivityPost",
			foreignKeyDefinition = """
					foreign key (activity_post_id)
					   references ActivityPost(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	@ToString.Exclude
	private ActivityPost activityPost;
}
