
package com.maveric.citi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler extends Exception {

	private static final long serialVersionUID = 1L;

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

	}

	@ExceptionHandler(AccountAlreadyExistException.class)
	public ResponseEntity<Object> handleAccountalreadyExist(AccountAlreadyExistException accountAlreadyExistException) {
		return new ResponseEntity<>("Account Already Exists", HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<String> handleInsufficientBalanceExceptionEntity(InsufficientBalanceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<String> AccounthandleNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

	}

	@ExceptionHandler(PropertyAlreadyExistException.class)
	public ResponseEntity<String> PropertyAlreadyExistException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());

	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials!!");

	}

}
