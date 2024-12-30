package com.example.trelloproject.user;

import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	//repository에서 이메일과 비밀번호 검증
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmailOrElseThrow(email);

		// UserDetails 객체 생성
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(),
			user.getPassword(),
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().toString())) // 권한 설정
		);
	}
}