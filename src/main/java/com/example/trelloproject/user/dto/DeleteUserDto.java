package com.example.trelloproject.user.dto;

import lombok.Getter;

@Getter
public class DeleteUserDto {
	private String password;

	public DeleteUserDto(String password) {
		this.password = password;
	}

}
