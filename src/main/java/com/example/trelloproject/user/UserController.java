package com.example.trelloproject.user;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.user.dto.DeleteUserDto;
import com.example.trelloproject.user.dto.JwtResponseDto;
import com.example.trelloproject.user.dto.LoginRequestDto;
import com.example.trelloproject.user.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletRequest;
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

//	@PostMapping("/login")
//	public ResponseEntity<MessageDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
//		HttpServletRequest request){
//		User user = userService.login(
//			loginRequestDto.getEmail(), loginRequestDto.getPassword()
//		);
//
//		HttpSession session = request.getSession(true);
//
//		if (session.getAttribute(Const.LOGIN_USER) != null) {
//			return new ResponseEntity<>(new MessageDto("이미 로그인 되어있는 사용자 입니다."), HttpStatus.CONFLICT);
//		}
//
//		session.setAttribute(Const.LOGIN_USER, user);
//		MessageDto message = new MessageDto("로그인이 완료되었습니다.");
//
//		return new ResponseEntity<>(message, HttpStatus.OK);
//	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
		JwtResponseDto jwtResponseDto = userService.login(loginRequestDto);
		return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<MessageDto> logout(HttpServletRequest request){
		//토큰 삭제는 클라이언트측에서 구현
		MessageDto message = new MessageDto("로그아웃이 완료되었습니다.");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<MessageDto> deleteUser(@RequestBody DeleteUserDto deleteUserDto,@RequestHeader("Authorization") String authorizationHeader){
		// Bearer 키워드 제거
		String token = authorizationHeader.replace("Bearer ", "");
		String email = jwtUtil.extractEmail(token);
		MessageDto message = userService.deleteUser(email,deleteUserDto.getPassword());
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
