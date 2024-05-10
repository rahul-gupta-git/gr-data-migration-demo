package com.rg.gr.demo.exception;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ProblemDetail onRuntimeException(RuntimeException ex) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		detail.setTitle("Bad Input");
		return detail;
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ProblemDetail onJsonProcessingException(JsonProcessingException ex) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		detail.setTitle("Json processing error");
		return detail;
	}

	@ExceptionHandler(SQLException.class)
	public ProblemDetail onSQLException(SQLException ex) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		detail.setTitle("SQL Exception occured");
		return detail;
	}
}
