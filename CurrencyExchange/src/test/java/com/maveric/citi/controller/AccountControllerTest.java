
package com.maveric.citi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.enums.DrCrEnum;
import com.maveric.citi.exception.AccountAlreadyExistException;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.service.IAccountService;
import com.maveric.citi.utility.AccountUtility;

public class AccountControllerTest {

	@InjectMocks
	private AccountController accountCreation;

	@Mock
	private IAccountService iAccountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateAccount_ValidRequest() throws Exception {

		AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest();
		accountDetailsRequest.setAccHolderName("Mokshi");
		accountDetailsRequest.setBalance(BigDecimal.valueOf(1000));
		accountDetailsRequest.setCurrencyType(CurrencyTypeEnum.INR);

		AccountDetailsResponse expectedResponse = new AccountDetailsResponse();
		expectedResponse.setAccountNumber(123L);
		expectedResponse.setAccHolderName(accountDetailsRequest.getAccHolderName());
		expectedResponse.setBalance(accountDetailsRequest.getBalance());
		expectedResponse.setCurrencyType(accountDetailsRequest.getCurrencyType());
		expectedResponse.setAccountCreationDate(LocalDateTime.now());

		Mockito.when(iAccountService.createAccount(anyLong(), any())).thenReturn(expectedResponse);

		AccountDetailsResponse response = accountCreation.createAccount(1L, accountDetailsRequest);

		assertEquals(expectedResponse, response); // Check the response body
		verify(iAccountService, times(1)).createAccount(anyLong(), any());
	}

	@Test
	void testCreateAccount_InvalidRequest() throws Exception {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setAccHolderName(""); // Invalid name
		request.setBalance(BigDecimal.valueOf(1000));
		request.setCurrencyType(CurrencyTypeEnum.USD);

		assertThrows(InvalidRequestException.class, () -> accountCreation.createAccount(1L, request));
		verify(iAccountService, never()).createAccount(anyLong(), any());
	}

	@Test
	void testCreateAccount_CustomerNotFoundException() throws Exception {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setAccHolderName("Mokshi");
		request.setBalance(BigDecimal.valueOf(1000));
		request.setCurrencyType(CurrencyTypeEnum.USD);

		when(iAccountService.createAccount(anyLong(), any())).thenThrow(CustomerNotFoundException.class);

		assertThrows(CustomerNotFoundException.class, () -> accountCreation.createAccount(1L, request));
		verify(iAccountService, times(1)).createAccount(anyLong(), any());
	}

	@Test
	void testCreateAccount_AccountAlreadyExistException() throws Exception {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setAccHolderName("Mokshi");
		request.setBalance(BigDecimal.valueOf(1000));
		request.setCurrencyType(CurrencyTypeEnum.USD);

		when(iAccountService.createAccount(anyLong(), any())).thenThrow(AccountAlreadyExistException.class);

		assertThrows(AccountAlreadyExistException.class, () -> accountCreation.createAccount(1L, request));
		verify(iAccountService, times(1)).createAccount(anyLong(), any());
	}

	@Test
	void testCreateAccount_AccountNotFoundException() throws Exception {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setAccHolderName("Mokshi");
		request.setBalance(BigDecimal.valueOf(1000));
		request.setCurrencyType(CurrencyTypeEnum.USD);

		when(iAccountService.createAccount(anyLong(), any())).thenThrow(AccountNotFoundException.class);

		assertThrows(AccountNotFoundException.class, () -> accountCreation.createAccount(1L, request));
		verify(iAccountService, times(1)).createAccount(anyLong(), any());
	}

	@Test
	void testFetchAccountByNumber_ValidRequest() throws AccountNotFoundException, CustomerNotFoundException {

		Long accNumber = 123L;
		Long customerId = 1L;
		AccountDetailsResponse expectedResponse = new AccountDetailsResponse();
		expectedResponse.setAccountNumber(accNumber);
		expectedResponse.setAccHolderName("Mokshi");
		expectedResponse.setBalance(BigDecimal.valueOf(1000));
		expectedResponse.setCurrencyType(CurrencyTypeEnum.INR);

		when(iAccountService.fetchAccountByNumber(accNumber, customerId)).thenReturn(expectedResponse);

		AccountDetailsResponse response = accountCreation.fetchAccountByNumber(accNumber, customerId);

		assertEquals(expectedResponse, response); // Check the response body
		verify(iAccountService, times(1)).fetchAccountByNumber(accNumber, customerId);
	}

	@Test
	void testFetchAccountByNumber_AccountNotFoundException()
			throws AccountNotFoundException, CustomerNotFoundException {

		Long accNumber = 123L;
		Long customerId = 1L;

		when(iAccountService.fetchAccountByNumber(accNumber, customerId)).thenThrow(AccountNotFoundException.class);

		assertThrows(AccountNotFoundException.class, () -> accountCreation.fetchAccountByNumber(accNumber, customerId));
		verify(iAccountService, times(1)).fetchAccountByNumber(accNumber, customerId);
	}

	@Test
	void testFetchAccountByNumber_OtherException() throws AccountNotFoundException, CustomerNotFoundException {

		Long accNumber = 123L;
		Long customerId = 1L;

		when(iAccountService.fetchAccountByNumber(accNumber, customerId)).thenThrow(RuntimeException.class);

		assertThrows(RuntimeException.class, () -> accountCreation.fetchAccountByNumber(accNumber, customerId));
		verify(iAccountService, times(1)).fetchAccountByNumber(accNumber, customerId);
	}

	@Test
	void testFetchAccountByNumber_BalanceZero() throws AccountNotFoundException, CustomerNotFoundException {

		Long accNumber = 123L;
		Long customerId = 1L;
		AccountDetailsResponse expectedResponse = new AccountDetailsResponse();
		expectedResponse.setAccountNumber(accNumber);
		expectedResponse.setAccHolderName("Mokshi");
		expectedResponse.setBalance(BigDecimal.ZERO); // Edge case: balance is zero
		expectedResponse.setCurrencyType(CurrencyTypeEnum.INR);

		when(iAccountService.fetchAccountByNumber(accNumber, customerId)).thenReturn(expectedResponse);

		AccountDetailsResponse response = accountCreation.fetchAccountByNumber(accNumber, customerId);

		assertEquals(expectedResponse, response); // Check the response body
		verify(iAccountService, times(1)).fetchAccountByNumber(accNumber, customerId);
	}

	@Test
	void testFetchAllAccounts_ValidRequest() throws AccountNotFoundException {

		List<AccountDetailsResponse> expectedResponses = new ArrayList<>();
		expectedResponses
				.add(createAccountDetailsResponse(123L, "Mokshi", BigDecimal.valueOf(1000), CurrencyTypeEnum.INR));
		expectedResponses
				.add(createAccountDetailsResponse(124L, "Tara", BigDecimal.valueOf(2000), CurrencyTypeEnum.USD));

		when(iAccountService.fetchAllAccounts()).thenReturn(expectedResponses);

		List<AccountDetailsResponse> responses = accountCreation.fetchAllAccounts();

		assertEquals(expectedResponses, responses); // Check the response body
		verify(iAccountService, times(1)).fetchAllAccounts();
	}

	@Test
	void testFetchAllAccounts_EmptyList() throws AccountNotFoundException {

		List<AccountDetailsResponse> expectedResponses = new ArrayList<>();

		when(iAccountService.fetchAllAccounts()).thenReturn(expectedResponses);

		List<AccountDetailsResponse> responses = accountCreation.fetchAllAccounts();

		assertEquals(expectedResponses, responses); // Check the response body
		verify(iAccountService, times(1)).fetchAllAccounts();
	}

	@Test
	    void testFetchAllAccounts_AccountNotFoundException() throws AccountNotFoundException {
	       
	        when(iAccountService.fetchAllAccounts()).thenThrow(AccountNotFoundException.class);

	        
	        assertThrows(AccountNotFoundException.class, () -> accountCreation.fetchAllAccounts());
	        verify(iAccountService, times(1)).fetchAllAccounts();
	    }

	@Test
	    void testFetchAllAccounts_OtherException() throws AccountNotFoundException {
	        
	        when(iAccountService.fetchAllAccounts()).thenThrow(RuntimeException.class);

	        
	        assertThrows(RuntimeException.class, () -> accountCreation.fetchAllAccounts());
	        verify(iAccountService, times(1)).fetchAllAccounts();
	    }

	private AccountDetailsResponse createAccountDetailsResponse(Long accountNumber, String holderName,
			BigDecimal balance, CurrencyTypeEnum currencyType) {
		AccountDetailsResponse response = new AccountDetailsResponse();
		response.setAccountNumber(accountNumber);
		response.setAccHolderName(holderName);
		response.setBalance(balance);
		response.setCurrencyType(currencyType);
		response.setAccountCreationDate(LocalDateTime.now());
		return response;
	}

	@Test
	void testUpdateAccountBalance_ValidRequest1() throws AccountNotFoundException, InvalidRequestException,
			InsufficientBalanceException, CustomerNotFoundException {

		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setAmount(BigDecimal.valueOf(500));
		request.setDrCr_flg(DrCrEnum.DEBIT); // Set the DrCr_flg field

		AccountDetailsResponse expectedResponse = new AccountDetailsResponse();
		expectedResponse.setAccountNumber(123L);
		expectedResponse.setAccHolderName("Mokshi");
		expectedResponse.setBalance(BigDecimal.valueOf(1500));
		expectedResponse.setCurrencyType(CurrencyTypeEnum.INR);
		expectedResponse.setAccountCreationDate(LocalDateTime.now());

		when(iAccountService.updateAccountBalance(anyLong(), anyLong(), any())).thenReturn(expectedResponse);

		AccountDetailsResponse response = accountCreation.updateAcccountBalance(1L, 123l, request);

		assertEquals(expectedResponse, response); // Check the response body
		verify(iAccountService, times(1)).updateAccountBalance(anyLong(), anyLong(), any());
	}

	@Test
	void testUpdateAccountBalance_InvalidRequest() throws AccountNotFoundException, InvalidRequestException,
			InsufficientBalanceException, CustomerNotFoundException {

		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setAmount(BigDecimal.valueOf(-100)); // Invalid amount

		assertThrows(InvalidRequestException.class, () -> accountCreation.updateAcccountBalance(1L, 123L, request));
		verify(iAccountService, never()).updateAccountBalance(anyLong(), anyLong(), any());
	}

	@Disabled
	@Test
	public void testDeleteAccountSuccess() throws AccountNotFoundException, CustomerNotFoundException {

		Long customerId = 1L;
		Long accountNumber = 123L;

		when(iAccountService.deleteAccount(customerId, accountNumber)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		ResponseEntity<?> response = accountCreation.deleteAccount(customerId, accountNumber);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Account deleted successfully", response.getBody());
	}

	@Test
	void testDeleteAccount_AccountNotFoundException() throws AccountNotFoundException, CustomerNotFoundException {
		Long accountNumber = 1L;
		Long customerId = 1L;
		when(iAccountService.deleteAccount(accountNumber, customerId)).thenThrow(AccountNotFoundException.class);

		assertThrows(AccountNotFoundException.class, () -> accountCreation.deleteAccount(customerId, accountNumber));
		verify(iAccountService, times(1)).deleteAccount(accountNumber, customerId);
	}

	@Test
	void testDeleteAccount_GeneralException() throws AccountNotFoundException, CustomerNotFoundException {

		Long accountNumber = 123L;
		Long customerId = 1L;
		when(iAccountService.deleteAccount(accountNumber, customerId)).thenThrow(RuntimeException.class);

		assertThrows(RuntimeException.class, () -> accountCreation.deleteAccount(accountNumber, customerId));
		verify(iAccountService, times(1)).deleteAccount(accountNumber, customerId);
	}

	@Test
	public void testFetchAccountByCustomer_Success() throws CustomerNotFoundException, AccountNotFoundException {
		// Arrange
		Long customerId = 1L;
		List<AccountDetailsResponse> accountDetailsList = new ArrayList<>(); // Create some account details here
		accountDetailsList.add(new AccountDetailsResponse());

		// Mock the service call
		when(iAccountService.fetchAccountByCustomer(customerId)).thenReturn(accountDetailsList);

		// Act
		ResponseEntity<List<AccountDetailsResponse>> response = accountCreation.fetchAccountByCustomer(customerId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(accountDetailsList, response.getBody());
	}

	@Test
	public void testFetchAccountByCustomer_CustomerNotFound()
			throws AccountNotFoundException, CustomerNotFoundException {
		// Arrange
		Long customerId = 1L;

		// Mock the service call to simulate customer not found
		when(iAccountService.fetchAccountByCustomer(customerId))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		// Act and Assert
		assertThrows(CustomerNotFoundException.class, () -> accountCreation.fetchAccountByCustomer(customerId));
	}

	@Test
	public void testFetchAccountByCustomer_AccountNotFound()
			throws AccountNotFoundException, CustomerNotFoundException {
		// Arrange
		Long customerId = 1L;

		// Mock the service call to simulate account not found
		when(iAccountService.fetchAccountByCustomer(customerId))
				.thenThrow(new AccountNotFoundException("Account not found"));

		// Act and Assert
		assertThrows(AccountNotFoundException.class, () -> accountCreation.fetchAccountByCustomer(customerId));
	}
	
//	@Test
//    public void testUpdateAccountBalance_InvalidRequest_new() throws InvalidRequestException {
//        // Arrange
//        Long customerId = 1L;
//        Long accountNumber = 123L;
//        AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
//        request.setAmount(BigDecimal.valueOf(100)); // Set an invalid amount
//
//        // Mock utility method to return false
//        when(AccountUtility.validateInputRequest(request)).thenReturn(false);
//
//        // Act and Assert
//        assertThrows(InvalidRequestException.class, () -> accountCreation.updateAcccountBalance(customerId, accountNumber, request));
//    }

}
