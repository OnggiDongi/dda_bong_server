package com.hana7.ddabong.entity;

import com.hana7.ddabong.enums.ApprovalStatus;

import org.hibernate.annotations.ColumnDefault;
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
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequest extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "supply", length = 20, nullable = false)
	private String supply;

	@Column(name="detail", length = 256)
	private String detail;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'PENDING'")
	private ApprovalStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "activity_post_id",
		foreignKey = @ForeignKey(
			name = "fk_SupportRequest_ActivityPost"
		)
	)
	private ActivityPost activityPost;

}
