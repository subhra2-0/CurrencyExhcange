package com.maveric.citi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;

public interface IAccountRepository extends JpaRepository<Account, Long>
{
 Optional<List<Account>> findByCustomer_customerId(Long customerId);
 List<Account> findByCustomer(Customer customer);
//Optional<Account> findByCustomerId(Long customerId);
 //@Query("from Account account join account.customer customer where account.currencyType=:currencyType and customer.customerId=:customerId")
 //Optional<Account> findByCurrencyTypeAndCustomerId(@Param("currencyType")CurrencyTypeEnum currencyType,@Param("customerId")long customerId) throws CustomerNotFoundException,AccountNotFoundException;
 Optional<Account> findByCurrencyTypeAndCustomer_customerId(CurrencyTypeEnum currencyType,Long customerId) throws CustomerNotFoundException,AccountNotFoundException;
   void deleteByCustomer_customerId(Long customerId);
}
