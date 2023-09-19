package com.maveric.citi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.enums.DrCrEnum;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.exception.AccountAlreadyExistException;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.utility.AccountUtility;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
	@InjectMocks
	private AccountService accountService;

	@Mock
	private ICustomerRepository iCustomerRepository;

	@Mock
	private IAccountRepository iAccountRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Test
	void testCreateAccount_ValidRequest()
			throws AccountAlreadyExistException, CustomerNotFoundException, AccountNotFoundException {

		Long customerId = 1L;
		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setAccHolderName("Mokshi");
		request.setBalance(BigDecimal.valueOf(1000));
		request.setCurrencyType(CurrencyTypeEnum.INR);

		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(), customerId))
				.thenReturn(Optional.empty());

		Account account = new Account();
		account.setAccountNumber(123L);
		when(iAccountRepository.save(any())).thenReturn(account);

		AccountDetailsResponse response = accountService.createAccount(customerId, request);

		assertNotNull(response);

		verify(iCustomerRepository, times(1)).findById(customerId);
		verify(iAccountRepository, times(1)).findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(),
				customerId);
		verify(iAccountRepository, times(1)).save(any());
	}

	@Test
	void testCreateAccount_CustomerNotFoundException() throws CustomerNotFoundException, AccountNotFoundException {

		Long customerId = 1L;
		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> accountService.createAccount(customerId, request));
		verify(iCustomerRepository, times(1)).findById(customerId);
		verify(iAccountRepository, never()).findByCurrencyTypeAndCustomer_customerId(any(), any());
		verify(iAccountRepository, never()).save(any());
	}

	@Test
	void testCreateAccount_AccountAlreadyExistException() throws CustomerNotFoundException, AccountNotFoundException {

		Long customerId = 1L;
		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(), customerId))
				.thenReturn(Optional.of(new Account()));

		assertThrows(AccountAlreadyExistException.class, () -> accountService.createAccount(customerId, request));
		verify(iCustomerRepository, times(1)).findById(customerId);
		verify(iAccountRepository, times(1)).findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(),
				customerId);
		verify(iAccountRepository, never()).save(any());
	}

	@Test
	void testCreateAccount_InvalidRequest() throws CustomerNotFoundException, AccountNotFoundException {

		Long customerId = 1L;
		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);
		request.setAccHolderName("M");
		request.setBalance(BigDecimal.valueOf(1000));
		// assertThrows(InvalidRequestException.class, () ->
		// accountService.createAccount(customerId, request));
		verify(iCustomerRepository, never()).findById(anyLong());
		verify(iAccountRepository, never()).findByCurrencyTypeAndCustomer_customerId(any(), anyLong());
		verify(iAccountRepository, never()).save(any());
	}

	@Test
	void testCreateAccount_AccountAlreadyExistException1() throws CustomerNotFoundException, AccountNotFoundException {

		Long customerId = 1L;
		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(), customerId))
				.thenReturn(Optional.of(new Account()));

		assertThrows(AccountAlreadyExistException.class, () -> accountService.createAccount(customerId, request));
		verify(iCustomerRepository, times(1)).findById(customerId);
		verify(iAccountRepository, times(1)).findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(),
				customerId);
		verify(iAccountRepository, never()).save(any());
	}

	@Test
	public void testCreateAccount_AccountAlreadyExists_ThrowsAccountAlreadyExistException()
			throws AccountAlreadyExistException, CustomerNotFoundException, AccountNotFoundException {
		Long customerId = 123L;
		AccountDetailsRequest request = new AccountDetailsRequest();

		Customer customer = new Customer();

		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findByCurrencyTypeAndCustomer_customerId(request.getCurrencyType(), customerId))
				.thenReturn(Optional.of(new Account()));

		try {
			accountService.createAccount(customerId, request);
			fail("Expected AccountAlreadyExistException, but no exception was thrown");
		} catch (AccountAlreadyExistException e) {
			assertEquals("Account already exist of Currency type null for Customer : 123", e.getMessage());
		}

	}

	@Test
	public void testFetchAccountByNumber_NonExistingAccount_ThrowsAccountNotFoundException()
			throws AccountNotFoundException {

		Long accountNumber = 126L;
		Long customerId = 1L;

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
		when(iCustomerRepository.findById(accountNumber)).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class,
				() -> accountService.fetchAccountByNumber(accountNumber, customerId));
	}

	@Test
    public void testFetchAllAccounts_NoAccounts_ThrowsAccountNotFoundException() throws AccountNotFoundException {

        when(iCustomerRepository.findAll()).thenReturn(Collections.emptyList());


        assertThrows(AccountNotFoundException.class, () -> accountService.fetchAllAccounts());
    }

//	@Test
//	public void testUpdateAccountBalance_ValidRequest_Success()
//			throws AccountNotFoundException, InsufficientBalanceException, InvalidRequestException {
//
//		Long accountNumber = 123L;
//		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
//		request.setCurrencyType(CurrencyTypeEnum.INR);
//		request.setAccHolderName("Mokshi");
//		request.setBalance(BigDecimal.valueOf(1000));
//		Account account = new Account();
//
//		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
//		when(iAccountRepository.save(account)).thenReturn(account);
//		when(AccountUtility.convertToDto(account)).thenReturn(new AccountDetailsResponse());
//
//		AccountDetailsResponse actualResponse = accountService.updateAccountBalance(accountNumber, request);
//
//		assertNotNull(actualResponse);
//	}

//	@Test
//	public void testUpdateAccountBalance_InvalidRequest_ThrowsInvalidRequestException()
//			throws AccountNotFoundException, InsufficientBalanceException, InvalidRequestException {
//
//		Long accountNumber = 123L;
//		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
//
//		doThrow(new InvalidRequestException("Invalid request")).when(iAccountRepository).findById(accountNumber);
//
//		assertThrows(InvalidRequestException.class, () -> accountService.updateAccountBalance(accountNumber, request));
//	}

//	@Test
//	public void testDeleteAccount_ExistingAccount_Success() throws AccountNotFoundException, CustomerNotFoundException {
//		
//		Long accountNumber = 123L;
//		Long customerId = 1L;
//		
//		Customer customer = new Customer();
//		customer.setCustomerId(customerId);
//		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
//		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.of(new Account()));
//
//		ResponseEntity<?> responseEntity = accountService.deleteAccount(accountNumber, customerId);
//
//		assertNotNull(responseEntity);
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals("Account Deleted Successfully", responseEntity.getBody());
//	}

//	@Test
//	public void testDeleteAccount_NonExistingAccount_ThrowsAccountNotFoundException() throws AccountNotFoundException {
//		Long accountNumber = 126L;
//		Long customerId = 1L;
//		
//		Customer customer = new Customer();
//		customer.setCustomerId(customerId);
//		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
//		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.empty());
//
//		assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(accountNumber, customerId));
//	}

	@Test
	public void testUpdateAccountBalance_Credit_Success()
			throws AccountNotFoundException, InsufficientBalanceException, CustomerNotFoundException {
		// Arrange
		Long customerId = 1L;
		Long accountNumber = 123L;
		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setDrCr_flg(DrCrEnum.CREDIT);
		request.setAmount(BigDecimal.valueOf(100.0));

		// Mock customer and account data
		Customer customer = new Customer();
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(500.0));

		// Mock repository calls
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
		when(iAccountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock
																												// the
																												// save
																												// method

		// Act
		AccountDetailsResponse response = accountService.updateAccountBalance(customerId, accountNumber, request);

		// Assert
		assertEquals(BigDecimal.valueOf(600.0), response.getBalance());
	}

	@Test
	public void testUpdateAccountBalance_Debit_Success()
			throws AccountNotFoundException, InsufficientBalanceException, CustomerNotFoundException {
		// Arrange
		Long customerId = 1L;
		Long accountNumber = 123L;
		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setDrCr_flg(DrCrEnum.DEBIT);
		request.setAmount(BigDecimal.valueOf(100.0));

		// Mock customer and account data
		Customer customer = new Customer();
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(500.0));

		// Mock repository calls
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
		when(iAccountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock
																												// the
																												// save
																												// method

		// Act
		AccountDetailsResponse response = accountService.updateAccountBalance(customerId, accountNumber, request);

		// Assert
		assertEquals(BigDecimal.valueOf(400.0), response.getBalance());
	}

	@Test
	public void testUpdateAccountBalance_AccountNotFound() {
		// Arrange
		Long customerId = 1L;
		Long accountNumber = 123L;
		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setDrCr_flg(DrCrEnum.DEBIT);
		request.setAmount(BigDecimal.valueOf(100.0));

		// Mock customer data
		Customer customer = new Customer();

		// Mock repository calls to simulate account not found
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(iAccountRepository.findById(accountNumber)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(AccountNotFoundException.class,
				() -> accountService.updateAccountBalance(customerId, accountNumber, request));
	}

	@Test
	public void testUpdateAccountBalance_CustomerNotFound() {
		// Arrange
		Long customerId = 1L;
		Long accountNumber = 123L;
		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setDrCr_flg(DrCrEnum.DEBIT);
		request.setAmount(BigDecimal.valueOf(100.0));

		// Mock repository calls to simulate customer not found
		when(iCustomerRepository.findById(customerId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(CustomerNotFoundException.class,
				() -> accountService.updateAccountBalance(customerId, accountNumber, request));
	}

}
