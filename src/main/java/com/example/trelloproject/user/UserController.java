package com.example.trelloproject.user;

import com.example.trelloproject.global.constant.Const;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.dto.DeleteUserDto;
import com.example.trelloproject.user.dto.LoginResquestDto;
import com.example.trelloproject.user.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

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

	@PostMapping("/login")
	public ResponseEntity<MessageDto> login(@Valid @RequestBody LoginResquestDto loginResquestDto,
		HttpServletRequest request){
		User user = userService.login(
			loginResquestDto.getEmail(), loginResquestDto.getPassword()
		);

		HttpSession session = request.getSession(true);

		if (session.getAttribute(Const.LOGIN_USER) != null) {
			return new ResponseEntity<>(new MessageDto("이미 로그인 되어있는 사용자 입니다."), HttpStatus.CONFLICT);
		}

		session.setAttribute(Const.LOGIN_USER, user);
		MessageDto message = new MessageDto("로그인이 완료되었습니다.");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<MessageDto> logout(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new RuntimeException("로그인 기록이 없습니다.");
		}

		if (session != null) {
			session.invalidate();
		}
		MessageDto message = new MessageDto("로그아웃이 완료되었습니다.");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<MessageDto> deleteUser(HttpServletRequest request, @RequestBody DeleteUserDto deleteUserDto){
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(Const.LOGIN_USER);
		MessageDto message = userService.deleteUser(user.getId(), deleteUserDto.getPassword());
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
