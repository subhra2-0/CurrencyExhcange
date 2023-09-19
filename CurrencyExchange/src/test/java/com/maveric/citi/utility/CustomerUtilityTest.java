package com.maveric.citi.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.exception.InvalidRequestException;

public class CustomerUtilityTest {

	@InjectMocks
	private CustomerUtility customerUtility;

	@Test
	void testConvertToEntity_ValidRequest() {

		CustomerDetailsRequest request = new CustomerDetailsRequest();
		request.setCustomerName("Mokshi");
		request.setEmail("Mokshi@maveric.com");
		request.setGender(GenderEnum.MALE);
		request.setRole(RoleEnum.CUSTOMER);

		Customer customer = customerUtility.convertToEntity(request);

		assertNotNull(customer);
		assertEquals("Mokshi", customer.getCustomerName());
		assertEquals("Mokshi@maveric.com", customer.getEmail());
		assertEquals(GenderEnum.MALE, customer.getGender());
		assertEquals(RoleEnum.CUSTOMER, customer.getRole());
	}

	@Test
	void testConvertToDto_ValidRequest() {

		Customer customer = new Customer();
		customer.setCustomerName("rass");
		customer.setEmail("rass@maveric.com");
		customer.setGender(GenderEnum.FEMALE);
		customer.setRole(RoleEnum.SUPERUSER);

		CustomerDetailsResponse response = customerUtility.convertToDto(customer);

		assertNotNull(response);
		assertEquals("rass", response.getCustomerName());
		assertEquals("rass@maveric.com", response.getEmail());
		assertEquals(GenderEnum.FEMALE, response.getGender());
		assertEquals(RoleEnum.SUPERUSER, response.getRole());
	}

	@Test
	void testValidateInputRequest_ValidRequest() throws InvalidRequestException {

		CustomerDetailsRequest request = new CustomerDetailsRequest();
		request.setCustomerName("Alice");
		request.setEmail("alice@example.com");
		request.setGender(GenderEnum.FEMALE);
		request.setRole(RoleEnum.CUSTOMER);

		boolean isValid = customerUtility.validateInputRequest(request);

		assertTrue(isValid);
	}

	@Test
	void testConvertToDtoList_ValidRequest() {

		Customer customer = new Customer();
		customer.setCustomerName("Bob");
		customer.setEmail("bob@example.com");
		customer.setGender(GenderEnum.MALE);
		customer.setRole(RoleEnum.CUSTOMER);

		List<Customer> customersList = Collections.singletonList(customer);

		List<CustomerDetailsResponse> responseList = customerUtility.convertToDtoList(customersList);

		assertNotNull(responseList);
		assertEquals(1, responseList.size());
		assertEquals("Bob", responseList.get(0).getCustomerName());
		assertEquals("bob@example.com", responseList.get(0).getEmail());
		assertEquals(GenderEnum.MALE, responseList.get(0).getGender());
		assertEquals(RoleEnum.CUSTOMER, responseList.get(0).getRole());
	}

	@Test
	void testValidateInputRequest_InvalidEmail() {

		CustomerDetailsRequest request = new CustomerDetailsRequest();
		request.setCustomerName("John");
		request.setEmail("invalid-email");
		request.setGender(GenderEnum.MALE);
		request.setRole(RoleEnum.CUSTOMER);

		assertThrows(InvalidRequestException.class, () -> customerUtility.validateInputRequest(request));
	}

	@Test
	void testConvertToDtoList_NullList() {

		assertThrows(NullPointerException.class, () -> customerUtility.convertToDtoList(null));
	}

	@Test
	void testConvertToDtoList_EmptyList() {

		List<Customer> customersList = Collections.emptyList();

		List<CustomerDetailsResponse> responseList = customerUtility.convertToDtoList(customersList);

		assertNotNull(responseList);
		assertTrue(responseList.isEmpty());
	}

}
