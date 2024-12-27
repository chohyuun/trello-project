package com.example.trelloproject.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
	NOT_ADMIN(HttpStatus.UNAUTHORIZED, "관리자만 이용할 수 있습니다."),
	NOT_MEMBER(HttpStatus.UNAUTHORIZED, "멤버만 이용할 수 있습니다."),
	NOT_CONSTRUCTOR(HttpStatus.UNAUTHORIZED, "생성자만 수정할 수 있습니다."),
	NOT_FIND_WORKSPACE(HttpStatus.NOT_FOUND, "워크 스페이스를 찾을 수 없습니다."),
	NOT_INVITE(HttpStatus.BAD_REQUEST, "초대되지 않았습니다."),
	NOT_FIND_MEMBER(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
	NOT_FIND_BOARD(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),
	NOT_FIND_LIST(HttpStatus.NOT_FOUND, "리스트를 찾을 수 없습니다."),
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
