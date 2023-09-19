package com.maveric.citi.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.exception.InvalidRequestException;

public class PaymentsUtility
{
	private PaymentsUtility() 
	{}
	
	public static CurrencyConversionResponse convertToDto(CurrencyConversion currencyConversion)
	{
		if (currencyConversion == null) {

			return new CurrencyConversionResponse();
		}


		CurrencyConversionResponse currencyConversionResponse = new CurrencyConversionResponse(); 
		BeanUtils.copyProperties(currencyConversion, currencyConversionResponse);
		return currencyConversionResponse;
	}
	public static CurrencyConversion convertToEntity(CurrencyConversionRequest currencyConversionRequest)
	{
		CurrencyConversion currencyConversion = new CurrencyConversion();
		BeanUtils.copyProperties(currencyConversionRequest, currencyConversion);
		return currencyConversion;
	}
	public static Boolean validateInputRequest(CurrencyConversionRequest currencyConversionRequest) throws InvalidRequestException
	{
		if(currencyConversionRequest.getDrAccount().equals(currencyConversionRequest.getCrAccount()))
		{
			throw new InvalidRequestException("Debit/Credit accounts cannot be same");
		}
		if(currencyConversionRequest.getConversionAmount().compareTo(new BigDecimal(1))< 0)
		{
			throw new InvalidRequestException("Amount cannot be 0 or less than 1");
		}
		
		return true;
	}
	
	public static Set<CurrencyConversionResponse> convertToDtoList(Set<CurrencyConversion> conversionList)
	{
		Set<CurrencyConversionResponse> currencyConversionList = new TreeSet<>();
		for(CurrencyConversion conversion: conversionList)
		{
			currencyConversionList.add(convertToDto(conversion));
		}
		return currencyConversionList;
	}
}
