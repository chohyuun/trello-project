package com.example.trelloproject.user;

import com.example.trelloproject.global.config.PasswordEncoder;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.global.filter.JwtFilter;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.user.dto.JwtResponseDto;
import com.example.trelloproject.user.dto.LoginRequestDto;
import com.example.trelloproject.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);



	//TODO : 글로벌 예외처리 작성시 예외처리 전체 수정

	@Transactional
	public MessageDto signUp(String email, String password, String userName, UserRole role) {
		if (userRepository.existsByEmail(email)){
			throw new BusinessException(ExceptionType.EXIST_USER);
		}

		String encoded = passwordEncoder.encode(password);
		User user = new User(
			email,encoded,userName,role
		);
		userRepository.save(user);

		return new MessageDto("회원가입이 완료되었습니다.");
	}

	public JwtResponseDto login(LoginRequestDto loginRequestDto) {
		logger.info("Login attempt for email: {}", loginRequestDto.getEmail());

		// 사용자 인증 처리
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
		);

		// 인증 성공 시 JWT 토큰 생성
		String token = jwtUtil.generateToken(loginRequestDto.getEmail());
		logger.info("Generated JWT Token: {}", token);  // 생성된 JWT 토큰을 로그에 출력


		// JwtResponseDto에 JWT 토큰을 담아서 반환
		return new JwtResponseDto(token);
	}

	@Transactional
	public MessageDto deleteUser(String email, String password) {
		User user = userRepository.findByEmailOrElseThrow(email);
		if (!passwordEncoder.matches(password, user.getPassword())){
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
		userRepository.delete(user);

		return new MessageDto("회원 탈퇴가 완료 되었습니다.");
	}
}
