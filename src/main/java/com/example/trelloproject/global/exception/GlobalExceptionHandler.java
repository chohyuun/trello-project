package com.example.trelloproject.global.exception;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {

		ExceptionType exceptionType = e.getExceptionType();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		ExceptionResponse exceptionResponse = new ExceptionResponse(
			exceptionType.getStatus(),exceptionType.getErrorMessage(),exceptionType.getStatus().value(), timestamp
		);

		return new ResponseEntity<>(exceptionResponse, exceptionType.getStatus());
	}
}
