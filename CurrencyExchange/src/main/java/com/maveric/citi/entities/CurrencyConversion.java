package com.maveric.citi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.maveric.citi.enums.CurrencyTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CurrencyConversion 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "txn_seq")
	@SequenceGenerator(name = "txn_seq", sequenceName = "transaction_sequence", initialValue = 317101, allocationSize = 1)
	private Long txnId;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private CurrencyTypeEnum drAccountType;
	@NotNull
	@Enumerated(EnumType.STRING)
	private CurrencyTypeEnum crAccountType;
	
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(precision = 38,scale = 5)
    private BigDecimal conversionAmount;

	@Column(updatable = false)
	private LocalDateTime transactionDate;

	@Column(updatable = false,precision = 38,scale = 7)
	private BigDecimal conversionUnitRate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerId")
	private Customer customer;

	@NotBlank
	private String remarks;
	@Override
	public String toString() {
		return "CurrencyConversion [txnId=" + txnId + ", drAccountType=" + drAccountType + ", crAccountType=" + crAccountType
				+ ", conversionAmount=" + conversionAmount + ", remarks=" + remarks + "]";
	}
	
	
}
