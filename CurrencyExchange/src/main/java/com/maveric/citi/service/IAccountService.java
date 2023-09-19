package com.maveric.citi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.exception.AccountAlreadyExistException;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;

public interface IAccountService {
	
	AccountDetailsResponse createAccount(Long customerId, AccountDetailsRequest accountDetailsRequest)throws CustomerNotFoundException,AccountAlreadyExistException,AccountNotFoundException;
	AccountDetailsResponse fetchAccountByNumber(Long accountNumber,Long customerId) throws AccountNotFoundException, CustomerNotFoundException;
	List<AccountDetailsResponse> fetchAllAccounts()throws AccountNotFoundException;
	List<AccountDetailsResponse> fetchAccountByCustomer(Long customerId) throws AccountNotFoundException, CustomerNotFoundException;
	AccountDetailsResponse updateAccountBalance(Long customerId,Long accountNumber, AccountBalanceDetailsRequest accountBalanceDetailsRequest) throws AccountNotFoundException, InsufficientBalanceException, CustomerNotFoundException;
	ResponseEntity<?> deleteAccount(Long customerId,Long accountNumber) throws AccountNotFoundException, CustomerNotFoundException;
}
