package com.example.trelloproject.global.dto;

import lombok.Getter;

@Getter
public class MessageDto {
	private String message;

	public MessageDto(String message) {
		this.message = message;
	}
}
