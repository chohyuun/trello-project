package com.example.trelloproject.global.exception;

import java.sql.Timestamp;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

	private HttpStatus error;
	private String message;
	private int status;
	private Timestamp timestamp;

	public ExceptionResponse(HttpStatus error, String message, int status, Timestamp timestamp) {
		this.error = error;
		this.message = message;
		this.status = status;
		this.timestamp = timestamp;
	}
}
