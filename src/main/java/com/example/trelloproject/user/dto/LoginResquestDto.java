package com.example.trelloproject.user.dto;

import lombok.Getter;

@Getter
public class LoginResquestDto {
	private String email;
	private String password;

	public LoginResquestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
