package com.example.trelloproject.user;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.user.dto.DeleteUserDto;
import com.example.trelloproject.user.dto.JwtResponseDto;
import com.example.trelloproject.user.dto.LoginRequestDto;
import com.example.trelloproject.user.dto.SignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private AuthenticationManager authenticationManager;

	/**
	 *
	 * @param signupRequestDto 회원가입 dto > email, password, name, role (email과 password에 검증)
	 * @return 회원가입 완료 메세지 반환
	 */
	@PostMapping("/signup")
	public ResponseEntity<MessageDto> signUp(
		@Valid @RequestBody SignupRequestDto signupRequestDto
	){
		MessageDto messageDto = userService.signUp(
			signupRequestDto.getEmail(), signupRequestDto.getPassword(),
			signupRequestDto.getUserName(), signupRequestDto.getRole()
		);
		return new ResponseEntity<>(messageDto, HttpStatus.CREATED);
	}

	/**
	 *
	 * @param loginRequestDto 로그인 dto > email, password
	 * @return 로그인 완료 메세지 반환
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
		JwtResponseDto jwtResponseDto = userService.login(loginRequestDto);
		return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
	}

	/**
	 * jwt 토큰의 삭제는 클라이언트 단에서 시행되는 로직이기 때문에,
	 * 현재 로그아웃은 컨트롤러 에서만 형식적으로 존재합니다.
	 * @return 로그아웃 완료 메세지 반환
	 */
	@PostMapping("/logout")
	public ResponseEntity<MessageDto> logout(){
		//토큰 삭제는 클라이언트측에서 구현
		MessageDto message = new MessageDto("로그아웃이 완료되었습니다.");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	/**
	 *
	 * @param deleteUserDto 회원 삭제 dto > password
	 * @param authorizationHeader jwt 토큰 추출
	 * @return 삭제 완료 메세지 반환
	 */
	@DeleteMapping
	public ResponseEntity<MessageDto> deleteUser(@RequestBody DeleteUserDto deleteUserDto,@RequestHeader("Authorization") String authorizationHeader){
		// Bearer 키워드 제거
		String token = authorizationHeader.replace("Bearer ", "");
		String email = jwtUtil.extractEmail(token);
		MessageDto message = userService.deleteUser(email,deleteUserDto.getPassword());
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
