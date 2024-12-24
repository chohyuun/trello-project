package com.example.trelloproject.user;

import com.example.trelloproject.global.config.PasswordEncoder;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	//TODO : 글로벌 예외처리 작성시 예외처리 전체 수정

	@Transactional
	public MessageDto signUp(String email, String password, String userName, UserRole role) {
		if (userRepository.existsByEmail(email)){
			throw new RuntimeException("Email already exists");
		}

		String encoded = passwordEncoder.encode(password);
		User user = new User(
			email,encoded,userName,role
		);
		userRepository.save(user);

		return new MessageDto("회원가입이 완료되었습니다.");
	}

	public User login(String email, String password) {
		User user = userRepository.findByEmailOrElseThrow(email);
		if (!passwordEncoder.matches(password, user.getPassword())){
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		if (user.getIsDelete()){
			throw new RuntimeException("적합하지 않은 사용자입니다.");
		}

		return user;
	}

	@Transactional
	public MessageDto deleteUser(Long id, String password) {
		User user = userRepository.findByIdOrElseThrow(id);
		if (!passwordEncoder.matches(password, user.getPassword())){
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		if (user.getIsDelete()){
			throw new RuntimeException("적합하지 않은 사용자입니다.");
		}
		userRepository.delete(user);

		return new MessageDto("회원 탈퇴가 완료 되었습니다.");
	}
}
