package com.maveric.citi.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.dto.CustomerDetailsResponse;
import com.maveric.citi.dto.CustomerResponse;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.exception.InvalidRequestException;

public class CustomerUtility {

	private CustomerUtility() {
		super();
	}
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


	public static Customer convertToEntity(CustomerDetailsRequest customerDetailsRequest) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDetailsRequest, customer);
		return customer;
	}

	public static CustomerDetailsResponse convertToDto(Customer customer) {
		CustomerDetailsResponse customerDetailsResponse = new CustomerDetailsResponse();
		BeanUtils.copyProperties(customer, customerDetailsResponse);
		return customerDetailsResponse;
	}
	
	public static CustomerResponse convertToCustomerResponseDto(Customer customer) {
		CustomerResponse customerDetailsResponse = new CustomerResponse();
		BeanUtils.copyProperties(customer, customerDetailsResponse);
		return customerDetailsResponse;
	}

	public static Boolean validateInputRequest(CustomerDetailsRequest customerDetailsRequest) throws InvalidRequestException {
		Boolean isValidRequest = true;
		if (customerDetailsRequest.getCustomerName().length() > 255)
			throw new InvalidRequestException("Customer name cannot be more than 255 words.");

		if(customerDetailsRequest.getEmail()!=null)
		{
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(customerDetailsRequest.getEmail());
			if(!matcher.matches())
				throw new InvalidRequestException("Invalid Email Address, Please provide a valid Email.");	
		}
		else
			throw new InvalidRequestException("Email Cannot Be null");
		
		if (!customerDetailsRequest.getGender().toString().equalsIgnoreCase(GenderEnum.FEMALE.toString())
				&& !customerDetailsRequest.getGender().toString().equalsIgnoreCase(GenderEnum.MALE.toString()))
			throw new InvalidRequestException("Invalid Gender, please specify proper Gender in Request. MALE/FEMALE");

		if (!customerDetailsRequest.getRole().toString().equalsIgnoreCase(RoleEnum.CUSTOMER.toString())
				&& !customerDetailsRequest.getRole().toString().equalsIgnoreCase(RoleEnum.SUPERUSER.toString()))
			throw new InvalidRequestException(
					"invalid Role for the Given Customer, Please provide a valid role. SUPERUSER/CUSTOMER ");

		return isValidRequest;
	}
	public static List<CustomerDetailsResponse> convertToDtoList(List<Customer> customersList)
	{
		List<CustomerDetailsResponse> customerDetailsResponseList = new ArrayList<>();
		for(Customer customer:customersList)
		{
			customerDetailsResponseList.add(convertToDto(customer));
		}
		return customerDetailsResponseList;
	}

}
