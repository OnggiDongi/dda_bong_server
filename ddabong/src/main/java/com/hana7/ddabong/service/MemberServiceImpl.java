package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.MemberDTO;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.enums.ErrorCode;
import com.hana7.ddabong.enums.ROLE;
import com.hana7.ddabong.exception.NotFoundException;
import com.hana7.ddabong.repository.InstitutionRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl  implements UserDetailsService {

	private final UserRepository userRepository;
	private final InstitutionRepository institutionRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername: " + username);
		User user = userRepository.findByEmail(username).orElse(null);
		Institution institution = institutionRepository.findByEmail(username).orElse(null);

		System.out.println(username);
		if(user == null) {
			if(institution == null){
				throw new UsernameNotFoundException(username);
			} else if(institution.getDeletedAt() == null){
				return new MemberDTO(institution.getEmail(), institution.getPassword(), institution.getName(), ROLE.ROLE_INSTITUTION.name(), false);
			}
		} else {
			System.out.println("user = " + user);
			if(user.getPreferredCategory() == null && user.getPreferredRegion() == null){
				return new MemberDTO(user.getEmail(), user.getPassword(), user.getName(),ROLE.ROLE_USER.name(), true);
			}
			return new MemberDTO(user.getEmail(), user.getPassword(), user.getName(),ROLE.ROLE_USER.name(), false);
		}
		return null;
	}
}
