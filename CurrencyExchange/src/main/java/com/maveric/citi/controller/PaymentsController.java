package com.maveric.citi.controller;

import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.service.ICustomerService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.service.IPaymentsService;



@RestController
@RequestMapping("/orders")
public class PaymentsController 
{
	@Autowired
	private IPaymentsService iPaymentsService;
	private ICustomerService iCustomerService;
	private static final Logger logger = LoggerFactory.getLogger(PaymentsController.class);
	@PostMapping("/placeorder")
	public CurrencyConversionResponse currencyConversion(@Valid @RequestBody CurrencyConversionRequest currencyConversionRequest) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException
	{
		logger.info("Inside Controller of Payments for currencyConversion ");
		return iPaymentsService.createConversion(currencyConversionRequest);
	}
	@GetMapping("/watchlist/{customerId}")
	public CustomerCurrencyConversionResponse orderWatchList(@PathVariable(name ="customerId") Long customerId) throws CustomerNotFoundException,AccountNotFoundException
	{
		return iPaymentsService.getOrderWatchList(customerId);
	}




}
