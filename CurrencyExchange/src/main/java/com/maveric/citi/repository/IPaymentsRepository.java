package com.maveric.citi.repository;


import java.util.Optional;
import java.util.Set;

import com.maveric.citi.enums.CurrencyTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import com.maveric.citi.entities.CurrencyConversion;

public interface IPaymentsRepository extends JpaRepository<CurrencyConversion, Long> {
	
	Optional<Set<CurrencyConversion>> findByCustomer_customerId(long customerId);

    void deleteByCustomer_customerId(Long customerId);

//    void deleteByCurrencyTypeEnum(CurrencyTypeEnum currencyType);

    void deleteByDrAccountTypeOrCrAccountType(CurrencyTypeEnum drCurrencyType, CurrencyTypeEnum crCurrencyType);
}
