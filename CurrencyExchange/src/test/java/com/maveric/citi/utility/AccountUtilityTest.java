package com.maveric.citi.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.enums.DrCrEnum;
import com.maveric.citi.exception.InvalidRequestException;

public class AccountUtilityTest {

	@InjectMocks
	private AccountUtility accountUtility;

	@Test
	void testConvertToDtoList_NullList() {

		assertThrows(NullPointerException.class, () -> accountUtility.convertToDtoList(null));
	}

	@Test
	void testConvertToDtoList_EmptyList1() {

		List<Account> accountsList = Collections.emptyList();

		List<AccountDetailsResponse> responseList = accountUtility.convertToDtoList(accountsList);

		assertNotNull(responseList);
		assertTrue(responseList.isEmpty());
	}

	@Test
	void testConvertToDtoList_NonEmptyList() {
		Account account1 = new Account();
		account1.setAccountNumber(1L);
		Account account2 = new Account();
		account2.setAccountNumber(2L);
		List<Account> accountsList = new ArrayList<>();
		accountsList.add(account1);
		accountsList.add(account2);

		List<AccountDetailsResponse> responseList = accountUtility.convertToDtoList(accountsList);

		assertNotNull(responseList);
		assertEquals(2, responseList.size());
	}

	@Test
	void testConvertToEntity() {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);
		request.setBalance(BigDecimal.valueOf(100));
		request.setAccHolderName("John");

		Account account = accountUtility.convertToEntity(request);

		assertNotNull(account);
		assertEquals(CurrencyTypeEnum.INR, account.getCurrencyType());
		assertEquals(BigDecimal.valueOf(100), account.getBalance());
		assertEquals("John", account.getAccHolderName());
	}

	@Test
	void testConvertToDto() {

		Account account = new Account();
		account.setCurrencyType(CurrencyTypeEnum.USD);
		account.setBalance(BigDecimal.valueOf(500));
		account.setAccHolderName("Alice");

		AccountDetailsResponse response = accountUtility.convertToDto(account);

		assertNotNull(response);
		assertEquals(CurrencyTypeEnum.USD, response.getCurrencyType());
		assertEquals(BigDecimal.valueOf(500), response.getBalance());
		assertEquals("Alice", response.getAccHolderName());
	}

	@Test
	void testConvertToDtoList_EmptyList() {

		List<Account> accountsList = Collections.emptyList();

		List<AccountDetailsResponse> responseList = accountUtility.convertToDtoList(accountsList);

		assertNotNull(responseList);
		assertTrue(responseList.isEmpty());
	}

	@Test
	void testConvertToDtoList_NonEmptyList1() {

		Account account1 = new Account();
		account1.setCurrencyType(CurrencyTypeEnum.PLN);
		Account account2 = new Account();
		account2.setCurrencyType(CurrencyTypeEnum.SGD);
		List<Account> accountsList = Arrays.asList(account1, account2);

		List<AccountDetailsResponse> responseList = accountUtility.convertToDtoList(accountsList);

		assertNotNull(responseList);
		assertEquals(2, responseList.size());
		assertEquals(CurrencyTypeEnum.PLN, responseList.get(0).getCurrencyType());
		assertEquals(CurrencyTypeEnum.SGD, responseList.get(1).getCurrencyType());
	}

	@Test
	void testValidateInputRequest_AccountDetailsRequest_Valid() throws InvalidRequestException {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);
		request.setBalance(BigDecimal.valueOf(100));
		request.setAccHolderName("John");

		boolean isValid = accountUtility.validateInputRequest(request);

		assertTrue(isValid);
	}

	@Test
	void testValidateInputRequest_AccountDetailsRequest_InvalidBalance() {

		AccountDetailsRequest request = new AccountDetailsRequest();
		request.setCurrencyType(CurrencyTypeEnum.INR);
		request.setBalance(BigDecimal.valueOf(-100));
		request.setAccHolderName("John");

		assertThrows(InvalidRequestException.class, () -> accountUtility.validateInputRequest(request));
	}

	@Test
	void testValidateInputRequest_AccountBalanceDetailsRequest_Valid() throws InvalidRequestException {
		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setAmount(BigDecimal.valueOf(200));
		request.setDrCr_flg(DrCrEnum.DEBIT);

		boolean isValid = accountUtility.validateInputRequest(request);

		assertTrue(isValid);
	}

	@Test
	void testValidateInputRequest_AccountBalanceDetailsRequest_InvalidAmount() {

		AccountBalanceDetailsRequest request = new AccountBalanceDetailsRequest();
		request.setAmount(BigDecimal.valueOf(-200));
		request.setDrCr_flg(DrCrEnum.DEBIT);

		assertThrows(InvalidRequestException.class, () -> accountUtility.validateInputRequest(request));
	}

}
