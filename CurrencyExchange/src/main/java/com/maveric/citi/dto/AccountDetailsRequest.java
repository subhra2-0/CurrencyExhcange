package com.maveric.citi.dto;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.maveric.citi.enums.CurrencyTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountDetailsRequest 
{
	@NotNull
	private String AccHolderName;
	@NotNull
	private BigDecimal balance = new BigDecimal(0);
	@NotNull
	private CurrencyTypeEnum currencyType;
	
	@Override
	public String toString() {
		return "AccountDto [ AccHolderName="
				+ AccHolderName + ", balance=" + balance + ", currencyType=" + currencyType + "]";
	}
	
	
}
