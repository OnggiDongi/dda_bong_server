package com.hana7.ddabong.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Getter
public class MemberDTO extends User {

	private String email;
	private String name;
	private String role;
	private boolean firstLogin;

	public MemberDTO(String email, String password, String name, String role, boolean firstLogin) {
		super(email, password, List.of(new SimpleGrantedAuthority(role)));
		this.email = email;
		this.name = name;
		this.role = role;
		this.firstLogin = firstLogin;
	}

	public Map<String, Object> getClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("name", name);
		map.put("role", role);
		map.put("firstLogin", firstLogin);

		return map;
	}
}
