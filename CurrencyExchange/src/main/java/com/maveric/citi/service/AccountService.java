package com.maveric.citi.service;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.maveric.citi.dto.ApiResponse;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.repository.IPaymentsRepository;
import io.swagger.v3.core.util.Json;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.DrCrEnum;
import com.maveric.citi.exception.AccountAlreadyExistException;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.utility.AccountUtility;

import javax.swing.text.html.Option;


@Service
public class AccountService implements IAccountService {
	@Autowired
	private IAccountRepository iAccountRepository;
	@Autowired
	private ICustomerRepository iCustomerRepository;

	@Autowired
	private IPaymentsRepository iPaymentsRepository;
	@Autowired
	private ModelMapper mapper;


	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Override
	public AccountDetailsResponse createAccount(Long customerId, AccountDetailsRequest accountDetailsRequest)
			throws AccountAlreadyExistException, CustomerNotFoundException,AccountNotFoundException {
		AccountDetailsResponse accountDetailsResponse= null;
		Optional<Customer> fetchedCustomer = iCustomerRepository.findById(customerId);
		if (fetchedCustomer.isEmpty())
			throw new CustomerNotFoundException(
					"Cannot Create Account, Customer not Found for Customer ID : " + customerId);
		else {
		Optional<Account> fetchedAccount = iAccountRepository.findByCurrencyTypeAndCustomer_customerId(accountDetailsRequest.getCurrencyType(),customerId);
	//	Optional<List<Account>> fetchedAccount = iAccountRepository.findByCustomerId(customerId);
			if (fetchedAccount.isPresent())
				throw new AccountAlreadyExistException("Account already exist of Currency type "
						+ accountDetailsRequest.getCurrencyType() + " for Customer : " + customerId);
			else
			{
				Account account = AccountUtility.convertToEntity(accountDetailsRequest);
				account.setCustomer(fetchedCustomer.get());
				account.setAccountCreationDate(LocalDateTime.now());
				
			  accountDetailsResponse = AccountUtility.convertToDto(iAccountRepository.save(account));
			 
			}
		}
		return accountDetailsResponse;
	}
	@Override
	public AccountDetailsResponse fetchAccountByNumber(Long accountNumber,Long customerId) throws AccountNotFoundException, CustomerNotFoundException {
		Optional<Customer> fetchedCustomer=iCustomerRepository.findById(customerId);
		if(fetchedCustomer.isPresent()) {
			Optional<Account> fetchedAccount = iAccountRepository.findById(accountNumber);
			if (fetchedAccount.isEmpty())
				throw new AccountNotFoundException("Account Not found for the Account Number :" + accountNumber);
			else
				return AccountUtility.convertToDto(fetchedAccount.get());
		}
		else{
			throw new CustomerNotFoundException("The customer is not found with customerId"+customerId);
		}
	}
	@Override
	public List<AccountDetailsResponse> fetchAllAccounts() throws AccountNotFoundException
	{
		List<Account> listOfAccounts = iAccountRepository.findAll();
		if(listOfAccounts.isEmpty())
			throw new AccountNotFoundException("No Records Found for Accounts");
		else
			return AccountUtility.convertToDtoList(listOfAccounts);
	}

	@Override
	public List<AccountDetailsResponse> fetchAccountByCustomer(Long customerId) throws AccountNotFoundException, CustomerNotFoundException {

		logger.info("Inside fetchAccountByCustomer Service for Customer "+customerId);

		Customer customer = iCustomerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer not found with given id "+customerId));

		Optional<List<Account>> accounts = Optional.ofNullable(iAccountRepository.findByCustomer(customer));

		logger.info("list of accountssssssss"+accounts.get().size());

		if(accounts.isEmpty()){
			throw new AccountNotFoundException("Account not found in the record");
		}
		List<AccountDetailsResponse> accountResponseDtos=accounts.get().stream().map((acc)->this.mapper.map(acc,AccountDetailsResponse.class)).collect(Collectors.toList());
		return accountResponseDtos;

	}

	@Override
	@Transactional
	public AccountDetailsResponse updateAccountBalance(Long customerId,Long accountNumber, AccountBalanceDetailsRequest accountBalanceDetailsRequest) throws AccountNotFoundException, InsufficientBalanceException, CustomerNotFoundException {

		Optional<Customer> fetchedCustomer = iCustomerRepository.findById(customerId);
		if(fetchedCustomer.isPresent()) {
			Optional<Account> fetchedAccount = iAccountRepository.findById(accountNumber);
			if (fetchedAccount.isEmpty())
				throw new AccountNotFoundException("Account not found for the Account Number : " + accountNumber);
			else {
				if (accountBalanceDetailsRequest.getDrCr_flg().equals(DrCrEnum.CREDIT)) {
					fetchedAccount.get().setBalance(fetchedAccount.get().getBalance().add(accountBalanceDetailsRequest.getAmount()));
				} else if (fetchedAccount.get().getBalance().compareTo(accountBalanceDetailsRequest.getAmount()) != -1) {
					fetchedAccount.get().setBalance(fetchedAccount.get().getBalance().subtract(accountBalanceDetailsRequest.getAmount()));
				}
				return AccountUtility.convertToDto(iAccountRepository.save(fetchedAccount.get()));
			}
		}else{
			throw new CustomerNotFoundException("Customer not found with Id:"+customerId);

		}
		
	}
	@Transactional
	@Override
	public ResponseEntity<ApiResponse> deleteAccount(Long customerId, Long accountNumber) throws AccountNotFoundException, CustomerNotFoundException {

		ApiResponse message = new ApiResponse();
		message.setMessage("Account deleted successfully!!");
		Optional<Customer> fetchedCustomer= iCustomerRepository.findById(customerId);
		if(fetchedCustomer.isPresent()) {
			Optional<Account> fetchedAccount = iAccountRepository.findById(accountNumber);
			if (fetchedAccount.isEmpty())
				throw new AccountNotFoundException("Account not found ");
			else {
				Optional<Set<CurrencyConversion>> optionalCurrencyConversionList =iPaymentsRepository.findByCustomer_customerId(customerId);
				if(optionalCurrencyConversionList.isPresent())
				{
					iPaymentsRepository.deleteByDrAccountTypeOrCrAccountType(fetchedAccount.get().getCurrencyType(),fetchedAccount.get().getCurrencyType());
				}
				iAccountRepository.deleteById(accountNumber);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		}
		else{
			throw new CustomerNotFoundException("Customer not found with id"+customerId);
		}
		
	}
}
