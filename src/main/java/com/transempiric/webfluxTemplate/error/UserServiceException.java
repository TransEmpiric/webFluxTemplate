package com.transempiric.webfluxTemplate.error;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus httpStatus;
	private final String message;

	public UserServiceException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	public String getMessage() {
		return message;
	}
}
