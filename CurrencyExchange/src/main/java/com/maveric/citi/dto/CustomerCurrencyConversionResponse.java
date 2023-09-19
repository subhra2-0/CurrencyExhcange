package com.maveric.citi.dto;

import java.util.Set;

import lombok.Data;

@Data
public class CustomerCurrencyConversionResponse {
	private CustomerResponse customerResponse;
	private Set<CurrencyConversionResponse> currencyConversionResponse;

	@Override
	public String toString() {
		return "CustomerCurrencyConversionResponse{" +
				"customerResponse=" + customerResponse +
				", currencyConversionResponse=" + currencyConversionResponse +
				'}';
	}
}
