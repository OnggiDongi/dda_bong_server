package com.hana7.ddabong.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.ColumnDefault;

import com.hana7.ddabong.enums.ApprovalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Applicant extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "hours", nullable = false, precision = 5, scale = 2)
	private BigDecimal hours;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'PENDING'")
	private ApprovalStatus status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "user_id",
		foreignKey = @ForeignKey(
			name = "fk_Applicant_User",
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
			name = "fk_Applicant_ActivityPost"
			// foreignKeyDefinition = """
			// 		foreign key (activity_post_id)
			// 		   references ActivityPost(id)
			// 		    on DELETE cascade on UPDATE cascade
			// 	"""
		)
	)
	@ToString.Exclude
	private ActivityPost activityPost;

}
