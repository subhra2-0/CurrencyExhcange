package com.maveric.citi.utility;

import java.math.BigDecimal;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ConversionRate 
{
	
	private Map<String,BigDecimal> rates;

	@Override
	public String toString() {
		return "ConversionRate [conversionRate=" + rates + "]";
	}
	
	
}
