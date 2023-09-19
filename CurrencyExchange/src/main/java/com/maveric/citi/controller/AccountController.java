package com.maveric.citi.controller;

import java.util.List;

import com.maveric.citi.service.AuthenticationServiceImpl;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.exception.AccountAlreadyExistException;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.service.IAccountService;
import com.maveric.citi.utility.AccountUtility;

@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private IAccountService iAccountService;

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@PostMapping("/create/{customerId}")
	public AccountDetailsResponse createAccount(@Valid @PathVariable(name = "customerId") Long customerId,
			@RequestBody AccountDetailsRequest accountDetailsRequest) throws CustomerNotFoundException,
			InvalidRequestException, AccountAlreadyExistException, AccountNotFoundException {
		Boolean isCorrectRequest = AccountUtility.validateInputRequest(accountDetailsRequest);
		if (Boolean.TRUE.equals(isCorrectRequest))
			return iAccountService.createAccount(customerId, accountDetailsRequest);
		else
			throw new InvalidRequestException("Invalid Request");
	}

	@GetMapping("/fetch/{customerId}/{accountNumber}")
	public AccountDetailsResponse fetchAccountByNumber(@PathVariable Long customerId,@PathVariable("accountNumber") Long accNumber)
			throws AccountNotFoundException, CustomerNotFoundException {
		return iAccountService.fetchAccountByNumber(customerId,accNumber);
	}
	@GetMapping("/fetchAll")
	public List<AccountDetailsResponse> fetchAllAccounts()throws AccountNotFoundException
	{
		return iAccountService.fetchAllAccounts();
	}
	@PutMapping("/update/{customerId}/{accountNumber}")
	public AccountDetailsResponse updateAcccountBalance(@PathVariable Long customerId,@PathVariable(name = "accountNumber") Long accountNumber, @RequestBody AccountBalanceDetailsRequest accountBalanceDetailsRequest) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException, CustomerNotFoundException {
		Boolean isValidRequest = AccountUtility.validateInputRequest(accountBalanceDetailsRequest);
		if(Boolean.TRUE.equals(isValidRequest))
			return iAccountService.updateAccountBalance(customerId,accountNumber,accountBalanceDetailsRequest);
		else
			throw new InvalidRequestException("Invalid request");
	}
	@DeleteMapping("/delete/{customerId}/{accountNumber}")
	public ResponseEntity<?> deleteAccount(@PathVariable Long customerId,@PathVariable(name = "accountNumber") Long accountNumber) throws AccountNotFoundException, CustomerNotFoundException {
		return iAccountService.deleteAccount(customerId,accountNumber);
	}

	@GetMapping("/fetchAll/{customerId}")
	ResponseEntity<List<AccountDetailsResponse>> fetchAccountByCustomer(@PathVariable Long customerId) throws CustomerNotFoundException, AccountNotFoundException {
		logger.info("Inside accounts Controller");
		List<AccountDetailsResponse> getAccountByCustomer=iAccountService.fetchAccountByCustomer(customerId);
		return new ResponseEntity<>(getAccountByCustomer, HttpStatus.OK);
	}
}
