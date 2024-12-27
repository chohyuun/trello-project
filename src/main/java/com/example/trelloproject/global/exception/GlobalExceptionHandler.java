package com.example.trelloproject.global.exception;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

	//카드 Exception 부분
	@ExceptionHandler(CardInvalidFileFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleInvalidFileFormat(CardInvalidFileFormatException e) {
		return new ExceptionResponse(
				HttpStatus.BAD_REQUEST,
				e.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				new Timestamp(System.currentTimeMillis())
		);
	}

	@ExceptionHandler(CardFileSizeExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleFileSizeExceeded(CardFileSizeExceededException e) {
		return new ExceptionResponse(
				HttpStatus.BAD_REQUEST,
				e.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				new Timestamp(System.currentTimeMillis())
		);
	}

	@ExceptionHandler(CardFileException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse handleFileException(CardFileException e) {
		return new ExceptionResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				new Timestamp(System.currentTimeMillis())
		);
	}
}
