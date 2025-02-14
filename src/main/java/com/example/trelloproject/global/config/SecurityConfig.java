package com.example.trelloproject.global.config;

import com.example.trelloproject.global.filter.JwtFilter;
import com.example.trelloproject.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter; // JWT 필터를 주입
	private final UserDetailService userDetailService; // UserDetailsService 주입

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder();
	}

	//사용자 인증 관리를 담당
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		//spring security에서 AuthenticationManagerBuilder를 가져옴
		AuthenticationManagerBuilder authenticationManagerBuilder =
			http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailService); // UserDetailsService 설정
		return authenticationManagerBuilder.build();
	}

	//spring security에서 http 요청을 처리하기 위한 필터 체인
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/users/login", "/users/signup").permitAll()
				.requestMatchers("/workspaces/create").hasRole("ADMIN")// 로그인, 회원가입은 인증 없이 접근 가능
				.anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT를 사용하므로 세션 비활성화
			.formLogin(form -> form.disable()) // 기본 폼 로그인 비활성화
			.httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
			.addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // JWT 필터를 인증 필터 앞에 추가

		return http.build();
	}
}
