package com.hana7.ddabong.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
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
}
