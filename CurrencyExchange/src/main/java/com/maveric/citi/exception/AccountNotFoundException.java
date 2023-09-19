package com.maveric.citi.exception;



public class AccountNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String ex)
	{
		super(ex);
	}
}
