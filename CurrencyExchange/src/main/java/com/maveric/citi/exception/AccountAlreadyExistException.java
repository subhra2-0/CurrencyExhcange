package com.maveric.citi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AccountAlreadyExistException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountAlreadyExistException(String ex)
	{
		super(ex);
	}
}
