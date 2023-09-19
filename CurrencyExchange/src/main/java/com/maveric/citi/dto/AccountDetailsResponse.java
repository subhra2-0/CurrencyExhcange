package com.maveric.citi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maveric.citi.enums.CurrencyTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountDetailsResponse 
{
//	private CustomerDetailsResponse customerDetailsResponse;
	private Long accountNumber;
	private String AccHolderName;
	private BigDecimal balance;
	private CurrencyTypeEnum currencyType;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private LocalDateTime accountCreationDate;

	@Override
	public String toString() {
		return "AccountDetailsResponse [accountNumber=" + accountNumber + ", AccHolderName=" + AccHolderName + ", balance="
				+ balance + ", currencyType=" + currencyType + ", accountCreationDate=" + accountCreationDate + "]";
	}
	
	
	
	
	
}
