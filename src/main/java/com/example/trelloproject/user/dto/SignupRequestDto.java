package com.example.trelloproject.user.dto;

import com.example.trelloproject.user.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

	@NotBlank(message = "이메일을 입력해 주세요.")
	@Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "비밀번호는 최소 8글자 이상이며, 영문, 숫자, 특수문자를 1개씩 포함해야합니다.")
	private String password;

	@NotBlank(message = "이름을 입력해 주세요.")
	private String userName;

	@NotNull
	private UserRole role;

	public SignupRequestDto(String email, String password, String getUsername, UserRole role) {
		this.email = email;
		this.password = password;
		this.userName = getUsername;
		this.role = role;
	}
}
