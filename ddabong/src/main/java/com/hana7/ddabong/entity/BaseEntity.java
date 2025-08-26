package com.hana7.ddabong.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
	// @CreationTimestam -> // Java(Hibernate)에서 관리하고 싶을 때,
	@Column( // DB에서 관리하고 싶을 때,
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
		insertable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	// @UpdateTimestamp -> // Java(Hibernate)에서 관리하고 싶을 때,
	@Column(// DB에서 관리하고 싶을 때,
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
		insertable = false,
		updatable = false
	)
	private LocalDateTime updatedAt;

	@Column(nullable = true)
	private LocalDateTime deletedAt;

	public void markDeleted() {
		this.deletedAt = LocalDateTime.now();
	}
}
