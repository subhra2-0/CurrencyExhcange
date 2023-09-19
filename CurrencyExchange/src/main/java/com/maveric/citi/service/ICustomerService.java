package com.maveric.citi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.PropertyAlreadyExistException;

public interface ICustomerService {

	CustomerDetailsResponse createCustomer(CustomerDetailsRequest customerDetailsRequest) throws PropertyAlreadyExistException;
	CustomerDetailsResponse fetchCustomerById(Long customerId)throws CustomerNotFoundException;
	List<CustomerDetailsResponse> fetchAllCustomer()throws CustomerNotFoundException;
	CustomerDetailsResponse updateCustomer(Long customerId, CustomerDetailsRequest customerDetailsRequest)throws CustomerNotFoundException,PropertyAlreadyExistException;
	ResponseEntity<?> deleteCustomerById(Long customerId)throws CustomerNotFoundException;
}
