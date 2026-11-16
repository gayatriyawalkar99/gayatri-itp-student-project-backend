package com.school.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ApiErrorResponse>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		List<ApiErrorResponse> errors = new ArrayList<ApiErrorResponse>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			ApiErrorResponse apiError = new ApiErrorResponse(error.getField(), error.getRejectedValue(),
					error.getDefaultMessage());
			errors.add(apiError);
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(StudentCustomException.class)
	public ResponseEntity<String> handleStudentCustomException(StudentCustomException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(UserCustomException.class)
	public ResponseEntity<String> handleUserCustomException(UserCustomException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);

	}

}
