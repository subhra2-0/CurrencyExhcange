package com.maveric.citi.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CurrencyConversionRequest {
	private Long drAccount;
	private Long crAccount;
	private BigDecimal conversionAmount;
}
