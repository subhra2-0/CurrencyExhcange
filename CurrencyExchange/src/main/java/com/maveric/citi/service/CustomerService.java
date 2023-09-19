package com.maveric.citi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.repository.IPaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.PropertyAlreadyExistException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.utility.AccountUtility;
import com.maveric.citi.utility.CustomerUtility;

import jakarta.transaction.Transactional;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private ICustomerRepository iCustomerRepository;
	
	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	IPaymentsRepository iPaymentsRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Override
	public CustomerDetailsResponse createCustomer(CustomerDetailsRequest customerDetailsRequest) throws PropertyAlreadyExistException
	{
		CustomerDetailsResponse customerDetailsResponse = null;
			Optional<Customer> customerRegisteredEmail = iCustomerRepository.findByEmail(customerDetailsRequest.getEmail());
			if(customerRegisteredEmail.isPresent())
				throw new PropertyAlreadyExistException("Email ID "+customerDetailsRequest.getEmail()+" is already Registered");
			else
			{
				Customer customer = CustomerUtility.convertToEntity(customerDetailsRequest);
				customer.setOnboardDate(LocalDateTime.now());
				customer.setRole(RoleEnum.CUSTOMER);
				customer.setPassword(passwordEncoder.encode(customerDetailsRequest.getPassword()));
				Customer savedCustomer = iCustomerRepository.save(customer);
				customerDetailsResponse = CustomerUtility.convertToDto(savedCustomer);
			}
		return customerDetailsResponse;
	}

	@Override
	public CustomerDetailsResponse fetchCustomerById(Long customerId) throws CustomerNotFoundException {

		CustomerDetailsResponse customerDetailsResponse = null;
		logger.info("customer details Response.....  "+customerDetailsResponse);

		Optional<Customer> fetchedCustomer = iCustomerRepository.findById(customerId);
		if(fetchedCustomer.isEmpty())
			throw new CustomerNotFoundException("Customer not found for the ID : "+customerId);
		else
		{
			customerDetailsResponse = CustomerUtility.convertToDto(fetchedCustomer.get());
		 Optional<List<Account>> taggedAccounts =  iAccountRepository.findByCustomer_customerId(customerId);
		 if(taggedAccounts.isPresent() && !taggedAccounts.get().isEmpty())	
		 {

			 customerDetailsResponse.setAccountDetailsResponse(AccountUtility.convertToDtoList(taggedAccounts.get()));
		 }

		 logger.info("The final customerDetailsResponse  "+customerDetailsResponse);
			return customerDetailsResponse;
		}
	}
	@Override
	public List<CustomerDetailsResponse> fetchAllCustomer()throws CustomerNotFoundException
	{
		List<CustomerDetailsResponse> customerDetailsResponses= new ArrayList<>();
		CustomerDetailsResponse customerDetailsResponse = null;
		List<Customer> customersList = iCustomerRepository.findAll();
		if(customersList.isEmpty())
			throw new CustomerNotFoundException("No Records of Customers found");
		else
		{
			for(Customer customer:customersList)
			{
				customerDetailsResponse= CustomerUtility.convertToDto(customer);
				customerDetailsResponse.setAccountDetailsResponse(AccountUtility.convertToDtoList(iAccountRepository.findByCustomer_customerId(customer.getCustomerId()).get()));
				customerDetailsResponses.add(customerDetailsResponse);
			}
		}
		return customerDetailsResponses;
	}


	@Override

	public CustomerDetailsResponse updateCustomer(Long customerId, CustomerDetailsRequest customerDetailsRequest)throws CustomerNotFoundException,PropertyAlreadyExistException

	{

		CustomerDetailsResponse updatedCustomerDetailsResponse = null;

		Optional<Customer> fetchedCustomer = iCustomerRepository.findById(customerId);

		if(fetchedCustomer.isEmpty())

			throw new CustomerNotFoundException("Customer not found for the Customer ID : "+customerId);

		else

		{

			if(customerDetailsRequest.getEmail()!=null)

			{

				Optional<Customer> customerEmail = iCustomerRepository.findByEmail(customerDetailsRequest.getEmail());

				if(customerEmail.isPresent() && customerEmail.get().getCustomerId()!=fetchedCustomer.get().getCustomerId())

					throw new PropertyAlreadyExistException("Email ID "+customerDetailsRequest.getEmail()+" is already Registered");

				else

				{

					fetchedCustomer.get().setCustomerName(customerDetailsRequest.getCustomerName());

					fetchedCustomer.get().setGender(customerDetailsRequest.getGender());

					fetchedCustomer.get().setRole(customerDetailsRequest.getRole());

					fetchedCustomer.get().setContactNumber(customerDetailsRequest.getContactNumber());

					fetchedCustomer.get().setEmail(customerDetailsRequest.getEmail());

					updatedCustomerDetailsResponse = CustomerUtility.convertToDto(iCustomerRepository.save(fetchedCustomer.get()));

				}

			}

		}

		return updatedCustomerDetailsResponse;

	}
	@Override
	@Transactional
	public ResponseEntity<?> deleteCustomerById(Long customerId) throws CustomerNotFoundException
	{
		Optional<Customer> isCustomer = iCustomerRepository.findById(customerId);
		if(isCustomer.isPresent())
		{

			Optional<List<Account>> fetchedAccounts = iAccountRepository.findByCustomer_customerId(customerId);
			if(fetchedAccounts.isPresent())
			{
				Optional<Set<CurrencyConversion>> optionalCurrencyConversionList =iPaymentsRepository.findByCustomer_customerId(customerId);
				if(optionalCurrencyConversionList.isPresent())
				{
					iPaymentsRepository.deleteByCustomer_customerId(customerId);
				}
				iAccountRepository.deleteByCustomer_customerId(customerId);
			}
			iCustomerRepository.deleteById(customerId);
//			return new ResponseEntity<>("Customer "+customerId+" Deleted Successfully",HttpStatus.OK);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new CustomerNotFoundException("Customer not found with ID : "+customerId);
	}
}
