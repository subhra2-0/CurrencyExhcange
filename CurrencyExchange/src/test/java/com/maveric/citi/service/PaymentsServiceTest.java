package com.maveric.citi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.maveric.citi.exception.InsufficientBalanceException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.maveric.citi.dto.CurrencyConversionRequest;
import com.maveric.citi.dto.CurrencyConversionResponse;
import com.maveric.citi.dto.CustomerCurrencyConversionResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.exception.AccountNotFoundException;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.repository.IPaymentsRepository;
import com.maveric.citi.utility.ConversionRate;

public class PaymentsServiceTest {

    @InjectMocks
    private PaymentsService currencyConversionService;

    @Mock
    private IAccountRepository iAccountRepository;

    @Mock
    private IPaymentsRepository iPaymentsRepository;

    @Mock
    private ICustomerRepository iCustomerRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateConversion_ValidRequest()
            throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        Account drAccount = new Account();
        drAccount.setCustomer(customer);
        drAccount.setCurrencyType(CurrencyTypeEnum.USD);
        drAccount.setBalance(new BigDecimal(100.00));
        when(iAccountRepository.findById(1L)).thenReturn(Optional.of(drAccount));

        Account crAccount = new Account();
        crAccount.setCustomer(customer);
        crAccount.setCurrencyType(CurrencyTypeEnum.INR);
        crAccount.setBalance(new BigDecimal(1000.00));
        when(iAccountRepository.findById(2L)).thenReturn(Optional.of(crAccount));

        ConversionRate conversionRate = new ConversionRate();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(CurrencyTypeEnum.INR.toString(), BigDecimal.valueOf(0.9));
        conversionRate.setRates(rates);
        when(restTemplate.getForObject(anyString(), eq(ConversionRate.class))).thenReturn(conversionRate);

        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setDrAccount(1L);
        request.setCrAccount(2L);
        request.setConversionAmount(BigDecimal.valueOf(100));
        CurrencyConversionResponse response = currencyConversionService.createConversion(request);

        assertNotNull(response);
        verify(iAccountRepository, times(1)).findById(1L);
        verify(iAccountRepository, times(1)).findById(2L);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(ConversionRate.class));
    }

    @Test
    public void testCreateConversion_ValidRequest_new()
            throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        Account drAccount = new Account();
        drAccount.setCustomer(customer);
        drAccount.setCurrencyType(CurrencyTypeEnum.USD);
        drAccount.setBalance(new BigDecimal(100.00));
        when(iAccountRepository.findById(1L)).thenReturn(Optional.of(drAccount));

        Account crAccount = new Account();
        crAccount.setCustomer(customer);
        crAccount.setCurrencyType(CurrencyTypeEnum.INR);
        crAccount.setBalance(new BigDecimal(1000.00));
        when(iAccountRepository.findById(2L)).thenReturn(Optional.of(crAccount));

        ConversionRate conversionRate = new ConversionRate();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(CurrencyTypeEnum.INR.toString(), BigDecimal.valueOf(0.9));
        conversionRate.setRates(rates);
        when(restTemplate.getForObject(eq("https://open.er-api.com/v6/latest/" + drAccount.getCurrencyType()),
                eq(ConversionRate.class))).thenReturn(conversionRate);

        CurrencyConversionResponse expectedResponse = new CurrencyConversionResponse(); // Create expected response here

        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setDrAccount(1L);
        request.setCrAccount(2L);
        request.setConversionAmount(BigDecimal.valueOf(100));

        // Perform the test
        CurrencyConversionResponse response = currencyConversionService.createConversion(request);

        // Assertions
        assertNotNull(response);
        // Add assertions for other fields in the response, if needed

        verify(iAccountRepository, times(1)).findById(1L);
        verify(iAccountRepository, times(1)).findById(2L);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(ConversionRate.class));

    }

    @Test
    void testCreateConversion_AccountNotFound() {

        when(iAccountRepository.findById(1L)).thenReturn(Optional.empty());


        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setDrAccount(1L);
        request.setCrAccount(2L);
        assertThrows(AccountNotFoundException.class, () -> currencyConversionService.createConversion(request));

        verify(iAccountRepository, times(1)).findById(1L);

        verify(restTemplate, never()).getForObject(anyString(), eq(ConversionRate.class));
    }

    @Test
    void testCreateConversion_ValidRequest1()
            throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Account drAccount = new Account();
        drAccount.setCustomer(customer);
        drAccount.setCurrencyType(CurrencyTypeEnum.USD);
        drAccount.setBalance(new BigDecimal(100.00));
        when(iAccountRepository.findById(1L)).thenReturn(Optional.of(drAccount));

        Account crAccount = new Account();
        crAccount.setCustomer(customer);
        crAccount.setCurrencyType(CurrencyTypeEnum.INR);
        crAccount.setBalance(new BigDecimal(1000.00));
        when(iAccountRepository.findById(2L)).thenReturn(Optional.of(crAccount));

        // Mock RestTemplate
        ConversionRate conversionRate = new ConversionRate();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(CurrencyTypeEnum.INR.toString(), BigDecimal.valueOf(0.9));
        conversionRate.setRates(rates);
        when(restTemplate.getForObject(anyString(), eq(ConversionRate.class))).thenReturn(conversionRate);

        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setDrAccount(1L);
        request.setCrAccount(2L);
        request.setConversionAmount(BigDecimal.valueOf(100));

        CurrencyConversionResponse response = currencyConversionService.createConversion(request);

        assertNotNull(response);
        verify(iAccountRepository, times(1)).findById(1L);
        verify(iAccountRepository, times(1)).findById(2L);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(ConversionRate.class));
    }

    @Test
    public void testGetOrderWatchList_Success() throws CustomerNotFoundException, AccountNotFoundException {

        Long customerId = 1L;

        Customer customer = new Customer();
        when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        List<Account> accounts = new ArrayList<>();
        when(iAccountRepository.findByCustomer_customerId(customerId)).thenReturn(Optional.of(accounts));

        Set<CurrencyConversion> currencyConversions = new HashSet<>();
        when(iPaymentsRepository.findByCustomer_customerId(customerId)).thenReturn(Optional.of(currencyConversions));

        CustomerCurrencyConversionResponse response = currencyConversionService.getOrderWatchList(customerId);

        assertNotNull(response);

    }

    @Test
    public void testGetOrderWatchList_CustomerNotFound() {

        Long customerId = 1L;

        when(iCustomerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> currencyConversionService.getOrderWatchList(customerId));
    }

    @Test
    public void testGetOrderWatchList_AccountNotFound() {

        Long customerId = 1L;

        when(iCustomerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(iAccountRepository.findByCustomer_customerId(customerId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> currencyConversionService.getOrderWatchList(customerId));
    }

}
