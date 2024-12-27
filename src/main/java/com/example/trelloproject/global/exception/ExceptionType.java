package com.example.trelloproject.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	EXIST_USER(HttpStatus.BAD_REQUEST,"이 email을 사용할 수 없습니다."),
	//cardException 관련
	INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다. (지원 형식: jpg, png, pdf, csv)"),
	FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기는 5MB를 초과할 수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "첨부파일을 찾을 수 없습니다."),
	FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다."),
	CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다."),
	//fake
	NOT_FIND_BOARD(HttpStatus.BAD_REQUEST,"보드를 찾을수 없습니다."),
	NOT_FIND_WORKSPACE(HttpStatus.BAD_REQUEST, "워크스페이스를 찾을수 없습니다."),
	ExceptionType(HttpStatus.BAD_REQUEST,"권한없음"),
	NOT_ADMIN(HttpStatus.BAD_REQUEST,"권한업슴"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"권한?"),
	NOT_FIND_MEMBER(HttpStatus.BAD_REQUEST,"맴버없음"),
	NOT_MEMBER(HttpStatus.BAD_REQUEST,"맴벙벗음"),
	NOT_FIND_LIST(HttpStatus.BAD_REQUEST,"리스트업승");



	private final HttpStatus status;
	private final String errorMessage;

	ExceptionType(HttpStatus status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}





}
