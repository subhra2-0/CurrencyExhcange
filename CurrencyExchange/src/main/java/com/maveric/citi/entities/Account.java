package com.maveric.citi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.maveric.citi.enums.CurrencyTypeEnum;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account 
{
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	@SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", initialValue = 2023080000, allocationSize = 1)
	private Long accountNumber;
	
	@Column(nullable = false)
	private String AccHolderName;
	
	@Column(nullable = false,precision = 38,scale = 5)
	private BigDecimal balance;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable =false)
	private CurrencyTypeEnum currencyType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerid")
	private Customer customer; 
	
	private LocalDateTime accountCreationDate;

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", AccHolderName=" + AccHolderName + ", balance=" + balance
				+ ", currencyType=" + currencyType + ", customerId=" + customer + ", accountCreationDate="
				+ accountCreationDate + "]";
	}
	
	

}
