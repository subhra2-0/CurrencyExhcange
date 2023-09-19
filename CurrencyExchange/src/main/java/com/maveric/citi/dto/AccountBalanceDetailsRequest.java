package com.maveric.citi.dto;

import java.math.BigDecimal;

import com.maveric.citi.enums.DrCrEnum;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountBalanceDetailsRequest 
{
	private BigDecimal amount;
	
	private DrCrEnum drCr_flg;
	

}
