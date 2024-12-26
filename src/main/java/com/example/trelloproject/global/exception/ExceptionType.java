package com.example.trelloproject.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	NOT_CONSTRUCTOR(HttpStatus.UNAUTHORIZED, "생성자만 수정할 수 있습니다."),
	NOT_FIND_WORKSPACE(HttpStatus.NOT_FOUND, "워크 스페이스를 찾을 수 없습니다."),
	NOT_FIND_BOARD(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),
	NOT_FIND_LIST(HttpStatus.NOT_FOUND, "리스트를 찾을 수 없습니다."),
	EXIST_USER(HttpStatus.BAD_REQUEST,"이 email을 사용할 수 없습니다.");

	private final HttpStatus status;
	private final String errorMessage;

	ExceptionType(HttpStatus status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}
}
