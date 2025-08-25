// package com.hana7.ddabong.dto;
//
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
//
// import com.hana7.ddabong.entity.User;
// import com.hana7.ddabong.enums.Category;
//
// import lombok.Getter;
//
// @Getter
// public class UserDTO extends User {
// 	private String email;
// 	private String name;
// 	private String phoneNumber;
// 	private int totalHour;
// 	private LocalDate birthdate;
// 	private boolean isKakao;
// 	private String preferredRegion;
// 	private String profileImage;
// 	private List<Category> preferredCategory = new ArrayList<>();
//
//
// 	public UserDTO(String email, String password, String nickname, List<String> roleNames) {
// 		super(email, password, roleNames.stream().map(SimpleGrantedAuthority::new).toList());
// 		this.email = email;
// 		this.nickname = nickname;
// 		this.roleNames = roleNames;
// 	}
// }
