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
public class CurrencyConversionResponse implements Comparable<CurrencyConversionResponse>
{

	@Override
	public int compareTo(CurrencyConversionResponse other) {
		
		return Double.compare(this.txnId, other.txnId);
	}
	private Long txnId;
	private CurrencyTypeEnum drAccountType;
	private CurrencyTypeEnum crAccountType;
	private BigDecimal conversionAmount;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private LocalDateTime transactionDate;
	private BigDecimal conversionUnitRate;
	private String remarks;

	@Override
	public String toString() {
		return "CurrencyConversionResponse{" +
				"txnId=" + txnId +
				", drAccountType=" + drAccountType +
				", crAccountType=" + crAccountType +
				", conversionAmount=" + conversionAmount +
				", transactionDate=" + transactionDate +
				", conversionUnitRate=" + conversionUnitRate +
				", remarks='" + remarks + '\'' +
				'}';
	}
}
