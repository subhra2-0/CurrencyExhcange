package com.maveric.citi.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.exception.InvalidRequestException;

import jakarta.persistence.EnumType;

public class PaymentsUtilityTest {

	@InjectMocks
	private PaymentsUtility paymentsUtility;

	@Test
	void testConvertToDto() {

		CurrencyConversion currencyConversion = new CurrencyConversion();
		currencyConversion.setDrAccountType(CurrencyTypeEnum.INR);
		currencyConversion.setCrAccountType(CurrencyTypeEnum.USD);
		currencyConversion.setConversionAmount(BigDecimal.valueOf(100));

		CurrencyConversionResponse response = paymentsUtility.convertToDto(currencyConversion);

		assertNotNull(response);

		assertEquals(BigDecimal.valueOf(100), response.getConversionAmount());
	}

	@Test
	void testConvertToEntity() {

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(456L);
		request.setConversionAmount(BigDecimal.valueOf(100));

		CurrencyConversion conversion = paymentsUtility.convertToEntity(request);

		assertNotNull(conversion);

		assertEquals(BigDecimal.valueOf(100), conversion.getConversionAmount());
	}

	@Test
	void testValidateInputRequest_Valid() throws InvalidRequestException {

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(456L);
		request.setConversionAmount(BigDecimal.valueOf(100));

		boolean isValid = paymentsUtility.validateInputRequest(request);

		assertTrue(isValid);
	}

	@Test
	void testValidateInputRequest_SameAccounts() {

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(123L);
		request.setConversionAmount(BigDecimal.valueOf(100));

		assertThrows(InvalidRequestException.class, () -> paymentsUtility.validateInputRequest(request));
	}

	@Test
	void testValidateInputRequest_InvalidAmount() {

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(456L);
		request.setConversionAmount(BigDecimal.valueOf(0));

		assertThrows(InvalidRequestException.class, () -> paymentsUtility.validateInputRequest(request));
	}
}
