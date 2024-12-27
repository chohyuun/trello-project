package com.example.trelloproject.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	EXIST_USER(HttpStatus.BAD_REQUEST,"이 email을 사용할 수 없습니다."),
	USER_NOT_FOUNT(HttpStatus.BAD_REQUEST, "user를 찾을 수 없습니다."),
	USER_DELETED(HttpStatus.BAD_REQUEST,"탈퇴된 user 입니다.");

	private final HttpStatus status;
	private final String errorMessage;

	ExceptionType(HttpStatus status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}
}
