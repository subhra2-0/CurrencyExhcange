package com.maveric.citi.exception;

public class CustomerNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String ex)
	{
		super(ex);
	}

}
