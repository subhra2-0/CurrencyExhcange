package com.maveric.citi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.maveric.citi.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.service.IPaymentsService;


public class PaymentsControllerTest {
	@Mock
	private IPaymentsService iPaymentsService;

	@InjectMocks
	private PaymentsController currencyConversionController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCurrencyConversion_ValidRequest()
			throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(456L);
		request.setConversionAmount(BigDecimal.valueOf(100));

		CurrencyConversionResponse expectedResponse = new CurrencyConversionResponse();

		expectedResponse.setConversionAmount(BigDecimal.valueOf(100));

		when(iPaymentsService.createConversion(request)).thenReturn(expectedResponse);

		CurrencyConversionResponse response = currencyConversionController.currencyConversion(request);

		assertEquals(expectedResponse, response);
		verify(iPaymentsService, times(1)).createConversion(request);
	}

	@Test
	void testCurrencyConversion_AccountNotFoundException()
			throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {
		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setDrAccount(123L);
		request.setCrAccount(456L);
		request.setConversionAmount(BigDecimal.valueOf(100));

		when(iPaymentsService.createConversion(request)).thenThrow(AccountNotFoundException.class);

		assertThrows(AccountNotFoundException.class, () -> currencyConversionController.currencyConversion(request));
		verify(iPaymentsService, times(1)).createConversion(request);
	}
	
	
	@Test
    public void testOrderWatchList_Success() throws CustomerNotFoundException, AccountNotFoundException {
        // Arrange
        Long customerId = 1L;
        CustomerCurrencyConversionResponse response = new CustomerCurrencyConversionResponse(); // Create a response object here

        // Mock the service call
        when(iPaymentsService.getOrderWatchList(customerId)).thenReturn(response);

        // Act
        CustomerCurrencyConversionResponse result = currencyConversionController.orderWatchList(customerId);

        // Assert
        assertEquals(response, result);
    }

    @Test
    public void testOrderWatchList_CustomerNotFound() throws CustomerNotFoundException, AccountNotFoundException {
        // Arrange
        Long customerId = 1L;

        // Mock the service call to simulate customer not found
        when(iPaymentsService.getOrderWatchList(customerId)).thenThrow(new CustomerNotFoundException("Customer not found"));

        // Act and Assert
        assertThrows(CustomerNotFoundException.class, () -> currencyConversionController.orderWatchList(customerId));
    }

    @Test
    public void testOrderWatchList_AccountNotFound() throws CustomerNotFoundException, AccountNotFoundException {
        // Arrange
        Long customerId = 1L;

        // Mock the service call to simulate account not found
        when(iPaymentsService.getOrderWatchList(customerId)).thenThrow(new AccountNotFoundException("Account not found"));

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> currencyConversionController.orderWatchList(customerId));
    }

}
