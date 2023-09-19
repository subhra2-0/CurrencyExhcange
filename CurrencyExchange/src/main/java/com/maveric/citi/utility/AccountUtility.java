package com.maveric.citi.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.maveric.citi.dto.AccountBalanceDetailsRequest;
import com.maveric.citi.dto.AccountDetailsRequest;
import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.enums.DrCrEnum;
import com.maveric.citi.exception.InvalidRequestException;

public class AccountUtility {
	
	
	public static Account convertToEntity(AccountDetailsRequest accountDetailsRequest) {
		Account account = new Account();
		BeanUtils.copyProperties(accountDetailsRequest, account);
		return account;
	}

	public static AccountDetailsResponse convertToDto(Account account) {
		AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();
		BeanUtils.copyProperties(account, accountDetailsResponse);
		return accountDetailsResponse;
	}
	
	public static List<AccountDetailsResponse> convertToDtoList(List<Account> accountsList)
	{
		List<AccountDetailsResponse> accountDetailsResponseList = new ArrayList<>();
		for(Account accounts:accountsList)
		{
			accountDetailsResponseList.add(convertToDto(accounts));
		}
		return accountDetailsResponseList;
	}

	
	
	public static Boolean validateInputRequest(AccountDetailsRequest accountDetailsRequest) throws InvalidRequestException
	{
		Boolean isValidRequest = true;
		
		CurrencyTypeEnum currency = accountDetailsRequest.getCurrencyType();
		
		if(accountDetailsRequest.getBalance()==null)
			throw new InvalidRequestException("Balance cannot be null, please provide balance.");
		else if(accountDetailsRequest.getBalance().longValue()<0)
			throw new InvalidRequestException("Invalid balance, Balance can't be Negative.");
		
		if(accountDetailsRequest.getAccHolderName()==null|| accountDetailsRequest.getAccHolderName().trim().length()==0)
			throw new InvalidRequestException("Account Holder Name cannot be null");
		else if(accountDetailsRequest.getAccHolderName().trim().length()<2)
			throw new InvalidRequestException("Account Holder Name must have more than 1 Character");
		else if(accountDetailsRequest.getAccHolderName().trim().length()>250)
			throw new InvalidRequestException("Account Holder name cannot exceed more than 250 characters");
		
		if(!(currency.equals(CurrencyTypeEnum.INR)||currency.equals(CurrencyTypeEnum.PLN)||currency.equals(CurrencyTypeEnum.SGD)||currency.equals(CurrencyTypeEnum.USD)))
			throw new InvalidRequestException("Invalid Currency Type, Please specify valid Currency Type.");
		
		return isValidRequest;
			
	}

	public static Boolean validateInputRequest(AccountBalanceDetailsRequest accountBalanceDetailsRequest) throws InvalidRequestException 
	{
		Boolean isValidRequest = true;
		
		if(accountBalanceDetailsRequest.getAmount().intValue()<0)
		{
			throw new InvalidRequestException("Amount should be Greater than 0");
		}
		if(!accountBalanceDetailsRequest.getDrCr_flg().equals(DrCrEnum.CREDIT) && !accountBalanceDetailsRequest.getDrCr_flg().equals(DrCrEnum.DEBIT))
		{
			throw new InvalidRequestException("Invalid operation, Please provide valid Operation for updating the Balance.");
		}	
		return isValidRequest;
		
	}
}
