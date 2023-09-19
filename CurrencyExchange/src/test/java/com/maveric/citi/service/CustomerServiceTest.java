package com.maveric.citi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.citi.dto.AccountDetailsResponse;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.enums.CurrencyTypeEnum;
import com.maveric.citi.repository.IPaymentsRepository;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.PropertyAlreadyExistException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.utility.CustomerUtility;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = {CustomerService.class})
@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;

	@MockBean
	private IAccountRepository iAccountRepository;

	@MockBean
	private ICustomerRepository iCustomerRepository;

	@MockBean
	private IPaymentsRepository iPaymentsRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;


	@Test
	void testCreateCustomer() throws PropertyAlreadyExistException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult = Optional.of(customer);
		when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
		assertThrows(PropertyAlreadyExistException.class,
				() -> customerService.createCustomer(new CustomerDetailsRequest()));
		verify(iCustomerRepository).findByEmail(Mockito.<String>any());
	}


    @Test
    void testCreateCustomer2() throws PropertyAlreadyExistException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		when(iCustomerRepository.save(Mockito.<Customer>any())).thenReturn(customer);
		Optional<Customer> emptyResult = Optional.empty();
		when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);
		when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
		CustomerDetailsResponse actualCreateCustomerResult = customerService.createCustomer(new CustomerDetailsRequest());
		assertEquals(RoleEnum.SUPERUSER, actualCreateCustomerResult.getRole());
		assertEquals(GenderEnum.MALE, actualCreateCustomerResult.getGender());
		assertEquals("00:00", actualCreateCustomerResult.getOnboardDate().toLocalTime().toString());
		assertEquals("subhra@gmail.com", actualCreateCustomerResult.getEmail());
		assertEquals("Subhrajit", actualCreateCustomerResult.getCustomerName());
		assertEquals(1L, actualCreateCustomerResult.getCustomerId().longValue());
		assertEquals(1L, actualCreateCustomerResult.getContactNumber().longValue());
		verify(iCustomerRepository).save(Mockito.<Customer>any());
		verify(iCustomerRepository).findByEmail(Mockito.<String>any());
		verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }





    @Test
    void testFetchCustomerById() throws CustomerNotFoundException {
		Optional<List<Account>> ofResult = Optional.of(new ArrayList<>());
		when(iAccountRepository.findByCustomer_customerId(Mockito.<Long>any())).thenReturn(ofResult);

		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult2 = Optional.of(customer);
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
		CustomerDetailsResponse actualFetchCustomerByIdResult = customerService.fetchCustomerById(1L);
		assertEquals(RoleEnum.SUPERUSER, actualFetchCustomerByIdResult.getRole());
		assertEquals(GenderEnum.MALE, actualFetchCustomerByIdResult.getGender());
		assertEquals("00:00", actualFetchCustomerByIdResult.getOnboardDate().toLocalTime().toString());
		assertEquals("subhra@gmail.com", actualFetchCustomerByIdResult.getEmail());
		assertEquals("Subhrajit", actualFetchCustomerByIdResult.getCustomerName());
		assertEquals(1L, actualFetchCustomerByIdResult.getCustomerId().longValue());
		assertEquals(1L, actualFetchCustomerByIdResult.getContactNumber().longValue());
		verify(iAccountRepository).findByCustomer_customerId(Mockito.<Long>any());
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testFetchCustomerById2() throws CustomerNotFoundException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Abhijit");
		customer.setEmail("abhi@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("abhijit");
		customer.setRole(RoleEnum.SUPERUSER);

		Account account = new Account();
		account.setAccHolderName("Ajay");
		account.setAccountCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		account.setAccountNumber(1234567890L);
		BigDecimal balance = BigDecimal.valueOf(1L);
		account.setBalance(balance);
		account.setCurrencyType(CurrencyTypeEnum.INR);
		account.setCustomer(customer);

		ArrayList<Account> accountList = new ArrayList<>();
		accountList.add(account);
		Optional<List<Account>> ofResult = Optional.of(accountList);
		when(iAccountRepository.findByCustomer_customerId(Mockito.<Long>any())).thenReturn(ofResult);

		Customer customer2 = new Customer();
		customer2.setAccount(new ArrayList<>());
		customer2.setContactNumber(1L);
		customer2.setCurrencyConversions(new ArrayList<>());
		customer2.setCustomerId(1L);
		customer2.setCustomerName("Sanjay");
		customer2.setEmail("sanjay@gmail.com");
		customer2.setGender(GenderEnum.MALE);
		customer2.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer2.setPassword("sanjay");
		customer2.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult2 = Optional.of(customer2);
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
		CustomerDetailsResponse actualFetchCustomerByIdResult = customerService.fetchCustomerById(1L);
		List<AccountDetailsResponse> accountDetailsResponse = actualFetchCustomerByIdResult.getAccountDetailsResponse();
		assertEquals(1, accountDetailsResponse.size());
		assertEquals(RoleEnum.SUPERUSER, actualFetchCustomerByIdResult.getRole());
		assertEquals(1L, actualFetchCustomerByIdResult.getContactNumber().longValue());
		assertEquals(GenderEnum.MALE, actualFetchCustomerByIdResult.getGender());
		assertEquals("00:00", actualFetchCustomerByIdResult.getOnboardDate().toLocalTime().toString());
		assertEquals("sanjay@gmail.com", actualFetchCustomerByIdResult.getEmail());
		assertEquals("Sanjay", actualFetchCustomerByIdResult.getCustomerName());
		assertEquals(1L, actualFetchCustomerByIdResult.getCustomerId().longValue());
		AccountDetailsResponse getResult = accountDetailsResponse.get(0);
		assertEquals("Ajay", getResult.getAccHolderName());
		assertEquals(CurrencyTypeEnum.INR, getResult.getCurrencyType());
		BigDecimal expectedBalance = balance.ONE;
		BigDecimal balance2 = getResult.getBalance();
		assertSame(expectedBalance, balance2);
		assertEquals(1234567890L, getResult.getAccountNumber().longValue());
		assertEquals("1970-01-01", getResult.getAccountCreationDate().toLocalDate().toString());
		assertEquals("1", balance2.toString());
		verify(iAccountRepository).findByCustomer_customerId(Mockito.<Long>any());
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }




    @Test
    void testFetchCustomerById4() throws CustomerNotFoundException {
		Optional<Customer> emptyResult = Optional.empty();
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
		assertThrows(CustomerNotFoundException.class, () -> customerService.fetchCustomerById(1L));
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testFetchAllCustomer() throws CustomerNotFoundException {
		when(iCustomerRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(CustomerNotFoundException.class, () -> customerService.fetchAllCustomer());
		verify(iCustomerRepository).findAll();
    }


    @Test
    void testFetchAllCustomer2() throws CustomerNotFoundException {
		Optional<List<Account>> ofResult = Optional.of(new ArrayList<>());
		when(iAccountRepository.findByCustomer_customerId(Mockito.<Long>any())).thenReturn(ofResult);

		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("No Records of Customers found");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);

		ArrayList<Customer> customerList = new ArrayList<>();
		customerList.add(customer);
		when(iCustomerRepository.findAll()).thenReturn(customerList);
		assertEquals(1, customerService.fetchAllCustomer().size());
		verify(iAccountRepository).findByCustomer_customerId(Mockito.<Long>any());
		verify(iCustomerRepository).findAll();
    }






    @Test
    void testUpdateCustomer() throws CustomerNotFoundException, PropertyAlreadyExistException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit ");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult = Optional.of(customer);
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertNull(customerService.updateCustomer(1L, new CustomerDetailsRequest()));
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }





    @Test
    void testUpdateCustomer3() throws CustomerNotFoundException, PropertyAlreadyExistException {
		Optional<Customer> emptyResult = Optional.empty();
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
		assertThrows(CustomerNotFoundException.class,
				() -> customerService.updateCustomer(1L, new CustomerDetailsRequest()));
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testUpdateCustomer4() throws CustomerNotFoundException, PropertyAlreadyExistException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult = Optional.of(customer);

		Customer customer2 = new Customer();
		customer2.setAccount(new ArrayList<>());
		customer2.setContactNumber(1L);
		customer2.setCurrencyConversions(new ArrayList<>());
		customer2.setCustomerId(1L);
		customer2.setCustomerName("Subhrajit");
		customer2.setEmail("subhra@gmail.com");
		customer2.setGender(GenderEnum.MALE);
		customer2.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer2.setPassword("subhrajit");
		customer2.setRole(RoleEnum.SUPERUSER);

		Customer customer3 = new Customer();
		customer3.setAccount(new ArrayList<>());
		customer3.setContactNumber(1L);
		customer3.setCurrencyConversions(new ArrayList<>());
		customer3.setCustomerId(1L);
		customer3.setCustomerName("Subhrajit");
		customer3.setEmail("subhra@gmail.com");
		customer3.setGender(GenderEnum.MALE);
		customer3.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer3.setPassword("subhrajit");
		customer3.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult2 = Optional.of(customer3);
		when(iCustomerRepository.save(Mockito.<Customer>any())).thenReturn(customer2);
		when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult2);
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		CustomerDetailsRequest customerDetailsRequest = CustomerDetailsRequest.builder()
				.contactNumber(1L)
				.customerName("Subhrajit")
				.email("subhra@gmail.com")
				.gender(GenderEnum.MALE)
				.password("subhrajit")
				.role(RoleEnum.SUPERUSER)
				.build();
		CustomerDetailsResponse actualUpdateCustomerResult = customerService.updateCustomer(1L, customerDetailsRequest);
		assertEquals(RoleEnum.SUPERUSER, actualUpdateCustomerResult.getRole());
		assertEquals(GenderEnum.MALE, actualUpdateCustomerResult.getGender());
		assertEquals("00:00", actualUpdateCustomerResult.getOnboardDate().toLocalTime().toString());
		assertEquals("subhra@gmail.com", actualUpdateCustomerResult.getEmail());
		assertEquals("Subhrajit", actualUpdateCustomerResult.getCustomerName());
		assertEquals(1L, actualUpdateCustomerResult.getCustomerId().longValue());
		assertEquals(1L, actualUpdateCustomerResult.getContactNumber().longValue());
		verify(iCustomerRepository).save(Mockito.<Customer>any());
		verify(iCustomerRepository).findByEmail(Mockito.<String>any());
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testUpdateCustomer5() throws CustomerNotFoundException, PropertyAlreadyExistException {
		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult = Optional.of(customer);
		Customer customer2 = mock(Customer.class);
		when(customer2.getCustomerId()).thenReturn(-1L);
		doNothing().when(customer2).setAccount(Mockito.<List<Account>>any());
		doNothing().when(customer2).setContactNumber(Mockito.<Long>any());
		doNothing().when(customer2).setCurrencyConversions(Mockito.<List<CurrencyConversion>>any());
		doNothing().when(customer2).setCustomerId(Mockito.<Long>any());
		doNothing().when(customer2).setCustomerName(Mockito.<String>any());
		doNothing().when(customer2).setEmail(Mockito.<String>any());
		doNothing().when(customer2).setGender(Mockito.<GenderEnum>any());
		doNothing().when(customer2).setOnboardDate(Mockito.<LocalDateTime>any());
		doNothing().when(customer2).setPassword(Mockito.<String>any());
		doNothing().when(customer2).setRole(Mockito.<RoleEnum>any());
		customer2.setAccount(new ArrayList<>());
		customer2.setContactNumber(1L);
		customer2.setCurrencyConversions(new ArrayList<>());
		customer2.setCustomerId(1L);
		customer2.setCustomerName("Subharjit");
		customer2.setEmail("subhra@gmail.com");
		customer2.setGender(GenderEnum.MALE);
		customer2.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer2.setPassword("subharjit");
		customer2.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult2 = Optional.of(customer2);
		when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult2);
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		CustomerDetailsRequest customerDetailsRequest = CustomerDetailsRequest.builder()
				.contactNumber(1L)
				.customerName("subhrajit")
				.email("subhra@gmail.com")
				.gender(GenderEnum.MALE)
				.password("subhrajit")
				.role(RoleEnum.SUPERUSER)
				.build();
		assertThrows(PropertyAlreadyExistException.class,
				() -> customerService.updateCustomer(1L, customerDetailsRequest));
		verify(iCustomerRepository).findByEmail(Mockito.<String>any());
		verify(iCustomerRepository).findById(Mockito.<Long>any());
		verify(customer2).getCustomerId();
		verify(customer2).setAccount(Mockito.<List<Account>>any());
		verify(customer2).setContactNumber(Mockito.<Long>any());
		verify(customer2).setCurrencyConversions(Mockito.<List<CurrencyConversion>>any());
		verify(customer2).setCustomerId(Mockito.<Long>any());
		verify(customer2).setCustomerName(Mockito.<String>any());
		verify(customer2).setEmail(Mockito.<String>any());
		verify(customer2).setGender(Mockito.<GenderEnum>any());
		verify(customer2).setOnboardDate(Mockito.<LocalDateTime>any());
		verify(customer2).setPassword(Mockito.<String>any());
		verify(customer2).setRole(Mockito.<RoleEnum>any());
    }


    @Test
    void testDeleteCustomerById() throws CustomerNotFoundException {
		doNothing().when(iAccountRepository).deleteByCustomer_customerId(Mockito.<Long>any());
		Optional<List<Account>> ofResult = Optional.of(new ArrayList<>());
		when(iAccountRepository.findByCustomer_customerId(Mockito.<Long>any())).thenReturn(ofResult);

		Customer customer = new Customer();
		customer.setAccount(new ArrayList<>());
		customer.setContactNumber(1L);
		customer.setCurrencyConversions(new ArrayList<>());
		customer.setCustomerId(1L);
		customer.setCustomerName("Subhrajit");
		customer.setEmail("subhra@gmail.com");
		customer.setGender(GenderEnum.MALE);
		customer.setOnboardDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		customer.setPassword("subhrajit");
		customer.setRole(RoleEnum.SUPERUSER);
		Optional<Customer> ofResult2 = Optional.of(customer);
		doNothing().when(iCustomerRepository).deleteById(Mockito.<Long>any());
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
		doNothing().when(iPaymentsRepository).deleteByCustomer_customerId(Mockito.<Long>any());
		Optional<Set<CurrencyConversion>> ofResult3 = Optional.of(new HashSet<>());
		when(iPaymentsRepository.findByCustomer_customerId(anyLong())).thenReturn(ofResult3);
		ResponseEntity<?> actualDeleteCustomerByIdResult = customerService.deleteCustomerById(1L);
		assertNull(actualDeleteCustomerByIdResult.getBody());
		assertEquals(200, actualDeleteCustomerByIdResult.getStatusCodeValue());
		assertTrue(actualDeleteCustomerByIdResult.getHeaders().isEmpty());
		verify(iAccountRepository).findByCustomer_customerId(Mockito.<Long>any());
		verify(iAccountRepository).deleteByCustomer_customerId(Mockito.<Long>any());
		verify(iCustomerRepository).findById(Mockito.<Long>any());
		verify(iCustomerRepository).deleteById(Mockito.<Long>any());
		verify(iPaymentsRepository).findByCustomer_customerId(anyLong());
		verify(iPaymentsRepository).deleteByCustomer_customerId(Mockito.<Long>any());
    }





    @Test
    void testDeleteCustomerById3() throws CustomerNotFoundException {
		Optional<Customer> emptyResult = Optional.empty();
		when(iCustomerRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
		assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(1L));
		verify(iCustomerRepository).findById(Mockito.<Long>any());
    }





}
