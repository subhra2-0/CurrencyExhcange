package com.maveric.citi.service;

import java.util.List;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;

public interface IPaymentsService {
	CurrencyConversionResponse createConversion(CurrencyConversionRequest currencyConversionRequest) throws AccountNotFoundException,InvalidRequestException,InsufficientBalanceException;
	CustomerCurrencyConversionResponse getOrderWatchList(Long customerId) throws CustomerNotFoundException,AccountNotFoundException;



//	CustomerCurrencyConversionResponse getOrderWatchList() throws AccountNotFoundException;
}
