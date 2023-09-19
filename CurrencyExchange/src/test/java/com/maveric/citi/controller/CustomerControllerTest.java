
package com.maveric.citi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.exception.CustomerNotFoundException;
import com.maveric.citi.exception.InvalidRequestException;
import com.maveric.citi.exception.PropertyAlreadyExistException;
import com.maveric.citi.repository.IAccountRepository;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.service.ICustomerService;
import com.maveric.citi.utility.CustomerUtility;

public class CustomerControllerTest {

	@Mock
	private ICustomerService iCustomerService;

	@Mock
	private ICustomerRepository iCustomerRepository;

	@Mock
	private IAccountRepository iAccountRepository;
	
	@Mock 
	private CustomerUtility customerUtility;

	@InjectMocks
	private CustomerController customerOnboarding;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void createCustomer_ValidRequest_ReturnsResponse()
			throws InvalidRequestException, PropertyAlreadyExistException {

		CustomerDetailsRequest request = new CustomerDetailsRequest();
		request.setContactNumber(1234567L);
		request.setCustomerName("Prudhvi");
		request.setEmail("prudhvi@maveric.com");
		request.setGender(GenderEnum.MALE);
		request.setRole(RoleEnum.CUSTOMER);

		CustomerDetailsResponse expectedResponse = new CustomerDetailsResponse();
		Mockito.when(iCustomerService.createCustomer(request)).thenReturn(expectedResponse);

		CustomerDetailsResponse response = customerOnboarding.createCustomer(request);

		assertNotNull(response);
		assertEquals(expectedResponse, response);
	}

	@Test
	public void fetchCustomerById_ValidId_ReturnsResponse() throws CustomerNotFoundException {

		Long customerId = 1L;
		CustomerDetailsResponse expectedResponse = new CustomerDetailsResponse();
		when(iCustomerService.fetchCustomerById(customerId)).thenReturn(expectedResponse);

		CustomerDetailsResponse response = customerOnboarding.fetchCustomerById(customerId);

		assertNotNull(response);
		assertEquals(expectedResponse, response);
	}

	@Test
	public void fetchCustomerById_NonexistentId_ThrowsCustomerNotFoundException() throws CustomerNotFoundException {
		Long customerId = 999L;
		when(iCustomerService.fetchCustomerById(customerId)).thenThrow(CustomerNotFoundException.class);

		assertThrows(CustomerNotFoundException.class, () -> customerOnboarding.fetchCustomerById(customerId));
	}

	@Test
	public void fetchAllCustomers_ReturnsListOfResponses() throws CustomerNotFoundException {

		List<CustomerDetailsResponse> expectedResponses = new ArrayList<>();
		CustomerDetailsResponse response1 = new CustomerDetailsResponse();
		CustomerDetailsResponse response2 = new CustomerDetailsResponse();
		expectedResponses.add(response1);
		expectedResponses.add(response2);

		when(iCustomerService.fetchAllCustomer()).thenReturn(expectedResponses);

		List<CustomerDetailsResponse> responses = customerOnboarding.fetchAllCustomers();

		assertNotNull(responses);
		assertEquals(expectedResponses.size(), responses.size());
		assertEquals(expectedResponses.get(0), responses.get(0));
		assertEquals(expectedResponses.get(1), responses.get(1));
	}

	@Test()
	public void updateCustomer_ValidRequest_ReturnsResponse()
			throws CustomerNotFoundException, InvalidRequestException, PropertyAlreadyExistException {
		Long customerId = 1L;
		CustomerDetailsRequest request = new CustomerDetailsRequest();
		request.setContactNumber(123456789L);
		request.setCustomerName("Mokshi");
		request.setEmail("Mokshi@maveric.com");
		request.setGender(GenderEnum.MALE);
		request.setRole(RoleEnum.CUSTOMER);
		CustomerDetailsResponse expectedResponse = new CustomerDetailsResponse();
		when(iCustomerService.updateCustomer(customerId, request)).thenReturn(expectedResponse);

		CustomerDetailsResponse response = customerOnboarding.updateCustomer(customerId, request);

		assertNotNull(response);
		assertEquals(expectedResponse, response);
	}

	@Disabled
	@Test
	public void testCreateCustomer_InvalidRequest() throws InvalidRequestException {
		// Arrange
		CustomerDetailsRequest request = new CustomerDetailsRequest(); // Create an invalid request object here
		request.setCustomerName("Supiee");
		request.setContactNumber(123456789L);
     	request.setEmail("Supiee@maverics.com");
		request.setGender(GenderEnum.MALE);
		request.setRole(RoleEnum.CUSTOMER);
		// Mock utility method to return false
		when(CustomerUtility.validateInputRequest(request)).thenReturn(false);

		// Act and Assert
		assertThrows(InvalidRequestException.class, () -> customerOnboarding.createCustomer(request));
	}

}
