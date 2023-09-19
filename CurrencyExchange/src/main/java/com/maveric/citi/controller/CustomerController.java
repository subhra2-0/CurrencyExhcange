package com.maveric.citi.controller;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.exception.PropertyAlreadyExistException;
import com.maveric.citi.service.AuthenticationServiceImpl;
import com.maveric.citi.service.ICustomerService;
import com.maveric.citi.utility.CustomerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController

@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private ICustomerService iCustomerService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


	@PostMapping("/create")
	public CustomerDetailsResponse createCustomer(@RequestBody CustomerDetailsRequest customerDetailsRequest)
			throws InvalidRequestException,PropertyAlreadyExistException {
		Boolean isCorrectRequest = CustomerUtility.validateInputRequest(customerDetailsRequest);
		if (Boolean.TRUE.equals(isCorrectRequest))
			return iCustomerService.createCustomer(customerDetailsRequest);
		else
			throw new InvalidRequestException("Invalid Request");
	}

	@GetMapping("/fetch/{customerId}")
	public CustomerDetailsResponse fetchCustomerById(@PathVariable(name = "customerId") Long customerId)
			throws CustomerNotFoundException {
		logger.info("fetching the customer by Id in controller");
		return iCustomerService.fetchCustomerById(customerId);

	}

	@GetMapping("/fetchAll")
	public List<CustomerDetailsResponse> fetchAllCustomers() throws CustomerNotFoundException {

		logger.info("fetching all the customerssssssssssssss");
		return iCustomerService.fetchAllCustomer();
	}

	@PutMapping("/update/{customerId}")
	public CustomerDetailsResponse updateCustomer(@PathVariable(name = "customerId") Long customerId,
			@RequestBody CustomerDetailsRequest customerDetailsRequest) throws CustomerNotFoundException,InvalidRequestException,PropertyAlreadyExistException {
		Boolean isCorrectRequest = CustomerUtility.validateInputRequest(customerDetailsRequest);
		if(Boolean.TRUE.equals(isCorrectRequest))
		{
			return iCustomerService.updateCustomer(customerId,customerDetailsRequest);
		}
		else
			throw new InvalidRequestException("Invalid Request");
	}

	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable(name = "customerId") Long customerId)throws CustomerNotFoundException {
		return iCustomerService.deleteCustomerById(customerId);
	}
}
