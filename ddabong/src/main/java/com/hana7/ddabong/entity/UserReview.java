package com.hana7.ddabong.entity;

import org.hibernate.annotations.ColumnDefault;

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
public class UserReview extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "health_status",nullable = false)
	@ColumnDefault("3")
	private int healthStatus;

	@Column(name = "diligence_level",nullable = false)
	@ColumnDefault("5")
	private int diligenceLevel;


	@Column(name = "attitude",nullable = false)
	@ColumnDefault("3")
	private int attitude;

	@Column(name = "memo",length = 255)
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "user_id",
		foreignKey = @ForeignKey(
			name = "fk_UserReview_User"
		)
	)
	@ToString.Exclude
	private User user;
}
