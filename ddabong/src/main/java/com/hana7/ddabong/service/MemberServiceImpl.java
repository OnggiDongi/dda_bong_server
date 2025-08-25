package com.hana7.ddabong.service;

import com.hana7.ddabong.dto.MemberDTO;
import com.hana7.ddabong.entity.Institution;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.repository.InstitutionRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl  implements UserDetailsService {

	private final UserRepository userRepository;
	private final InstitutionRepository institutionRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElse(null);
		Institution institution = institutionRepository.findByEmail(username).orElse(null);

		if(user == null && institution == null) {
			throw new UsernameNotFoundException(username);
		} else if(user == null) {
			return new MemberDTO(institution.getEmail(), institution.getPassword(), institution.getName());
		} else{
			return new MemberDTO(user.getEmail(), user.getPassword(), user.getName());
		}
	}
}
