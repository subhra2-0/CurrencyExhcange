package com.maveric.citi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import com.maveric.citi.controller.AccountController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.dto.CustomerResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InsufficientBalanceException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.repository.IPaymentsRepository;
import com.maveric.citi.utility.ConversionRate;
import com.maveric.citi.utility.CustomerUtility;
import com.maveric.citi.utility.PaymentsUtility;

import jakarta.transaction.Transactional;

@Service
public class PaymentsService implements IPaymentsService {
	
	@Autowired
	private IPaymentsRepository iPaymentsRepository;
	
	@Autowired
	private IAccountRepository iAccountRepository;
	
	@Autowired
	private ICustomerRepository iCustomerRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(PaymentsService.class);
	
	@Override
	@Transactional
	public CurrencyConversionResponse createConversion(CurrencyConversionRequest currencyConversionRequest) throws AccountNotFoundException, InvalidRequestException,InsufficientBalanceException
	{
		logger.info("inside createConversion Service Method");
		Optional<Account> fetchedDrAccount = iAccountRepository.findById(currencyConversionRequest.getDrAccount());
		Optional<Account> fetchedCrAccount = iAccountRepository.findById(currencyConversionRequest.getCrAccount());
		CurrencyConversion currencyConversion =new CurrencyConversion();
		if(fetchedDrAccount.isPresent() && fetchedCrAccount.isPresent())
		{
			logger.info("inside If condition for dr and cr accounts present");
			if (fetchedDrAccount.get().getCurrencyType().equals(fetchedCrAccount.get().getCurrencyType()))
				throw new InvalidRequestException("Debit/Credit Currencies cannot be same");
			else {
				if (!fetchedDrAccount.get().getCustomer().getCustomerId().equals(fetchedCrAccount.get().getCustomer().getCustomerId()))
					throw new InvalidRequestException("Debit/Credit AccountHolders cannot be different");
				else if (fetchedDrAccount.get().getBalance().compareTo(currencyConversionRequest.getConversionAmount()) < 0)
					throw new InsufficientBalanceException("Cannot Initiate Currency conversion, Insufficient Balance in the account :" + fetchedDrAccount.get().getAccountNumber());
				else
				{
					CurrencyTypeEnum drCurrencyType = fetchedDrAccount.get().getCurrencyType();
					CurrencyTypeEnum crCurrencyType = fetchedCrAccount.get().getCurrencyType();
					logger.info("Before RestTemplate call");
					ConversionRate conversionRate = restTemplate.getForObject("https://open.er-api.com/v6/latest/" + drCurrencyType, ConversionRate.class);
					logger.info("After RestTemplate call");
					BigDecimal currencyUnitRate = conversionRate.getRates().get(crCurrencyType.toString());
					fetchedDrAccount.get().setBalance(fetchedDrAccount.get().getBalance().subtract(currencyConversionRequest.getConversionAmount()));
					iAccountRepository.save(fetchedDrAccount.get());
					fetchedCrAccount.get().setBalance(fetchedCrAccount.get().getBalance().add(currencyUnitRate.multiply(currencyConversionRequest.getConversionAmount())));
					iAccountRepository.save(fetchedCrAccount.get());
					currencyConversion.setCustomer(fetchedDrAccount.get().getCustomer());
					currencyConversion.setDrAccountType(drCurrencyType);
					currencyConversion.setCrAccountType(crCurrencyType);
					currencyConversion.setConversionAmount(currencyConversionRequest.getConversionAmount());
					currencyConversion.setConversionUnitRate(currencyUnitRate);
					currencyConversion.setTransactionDate(LocalDateTime.now());
					currencyConversion.setRemarks("Currency Conversion :::::::: " + drCurrencyType + " to " + crCurrencyType);
				}
			}
		}
		else
			throw new AccountNotFoundException("Debit/Credit Account Not found");
		return PaymentsUtility.convertToDto(iPaymentsRepository.save(currencyConversion));
	}
	
	@Override
	public CustomerCurrencyConversionResponse getOrderWatchList(Long customerId) throws CustomerNotFoundException,AccountNotFoundException
	{
		Optional<Set<CurrencyConversion>> currencyConversion = null;

		Comparator<CurrencyConversionResponse> dateComparator = Comparator.comparing(CurrencyConversionResponse::getTxnId).reversed();
		Set<CurrencyConversionResponse> currencyConversionResponseSet=new TreeSet<>(dateComparator);
		CustomerCurrencyConversionResponse customerCurrencyConversionResponseSet = new CustomerCurrencyConversionResponse();
		Optional<Customer> fetchedCustomer = iCustomerRepository.findById(customerId);
		if(fetchedCustomer.isEmpty())
			throw new CustomerNotFoundException("Customer not found");
		else
		{
			customerCurrencyConversionResponseSet.setCustomerResponse(CustomerUtility.convertToCustomerResponseDto(fetchedCustomer.get()));
			Optional<List<Account>> fetchedAccounts = iAccountRepository.findByCustomer_customerId(customerId);
			 if(fetchedAccounts.isPresent())
			 {
				for(Account acc : fetchedAccounts.get())
				{
					currencyConversion =iPaymentsRepository.findByCustomer_customerId(customerId);
					currencyConversionResponseSet.addAll(PaymentsUtility.convertToDtoList(currencyConversion.get()));

				}
				 customerCurrencyConversionResponseSet.setCurrencyConversionResponse(currencyConversionResponseSet);
				logger.info("currency conversion History :"+customerCurrencyConversionResponseSet);
			 }
			 else
				 throw new AccountNotFoundException("Account Not found for Customer"+customerId);
		}
		return customerCurrencyConversionResponseSet;
	}
}
