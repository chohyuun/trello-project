package com.example.trelloproject.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
	private ExceptionType exceptionType;

	public BusinessException(ExceptionType exceptionType) {
		super(exceptionType != null ? exceptionType.getErrorMessage() : "exceptionType이 비어있습니다.");
		this.exceptionType = exceptionType;
	}
}
