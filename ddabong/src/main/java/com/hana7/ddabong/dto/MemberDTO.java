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

	public MemberDTO(String email, String password, String name) {
		super(email, password, new ArrayList<>());
		this.email = email;
		this.name = name;
	}

	public Map<String, Object> getClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("name", name);

		return map;
	}
}
