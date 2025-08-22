package com.hana7.ddabong.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Institution extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="name", length = 30, nullable = false)
	private String name;

	@Column(name="email", length = 30, nullable = false)
	private String email;

	@Column(name = "password", length = 60, nullable = false)
	private String password;

	@Column(name = "phone_number", length = 13, nullable = false)
	private String phoneNumber;

	@Column(name = "detail")
	private String detail;

	@OneToMany(
		mappedBy = "institution"
	)
	@Builder.Default
	@ToString.Exclude
	private List<Activity> activities = new ArrayList<>();
}
