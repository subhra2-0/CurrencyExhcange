package com.maveric.citi.service;

import com.maveric.citi.dto.CustomerDetailsRequest;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.jwtuser.SigninRequest;
import com.maveric.citi.jwtuser.response.JwtAuthenticationTokenResponse;
import com.maveric.citi.repository.ICustomerRepository;
import com.maveric.citi.service.AuthenticationService;
import com.maveric.citi.service.AuthenticationServiceImpl;
import com.maveric.citi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthencationServiceTest {

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Mock
	private ICustomerRepository customerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Mock
	private AuthenticationManager authenticationManager;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSignin_ValidCredentials() {

		SigninRequest request = new SigninRequest("prudhvi@maveric.com", "kkk");

		Customer mockCustomer = new Customer();
		mockCustomer.setCustomerId(1L);
		mockCustomer.setEmail("prudhvi@maveric.com");
		mockCustomer.setPassword("hashedPassword");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(mockCustomer));
		when(jwtService.generateToken(any(Customer.class))).thenReturn("jwtToken");

		JwtAuthenticationTokenResponse response = authenticationService.signin(request);
		assertNotNull(response);
		assertEquals("jwtToken", response.getToken());

		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(customerRepository, times(1)).findByEmail(request.getEmail());
		verify(jwtService, times(1)).generateToken(any(Customer.class));
	}

	@Test
	public void testSignin_InvalidCredentials() {

		SigninRequest request = new SigninRequest("nonexistent@example.com", "invalidpassword");
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new IllegalArgumentException("User not found"));

		assertThrows(IllegalArgumentException.class, () -> authenticationService.signin(request));

		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(customerRepository, never()).findByEmail(request.getEmail());
		verify(jwtService, never()).generateToken(any(Customer.class));
	}

}
