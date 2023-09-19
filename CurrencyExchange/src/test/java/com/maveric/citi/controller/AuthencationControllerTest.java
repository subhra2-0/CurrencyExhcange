package com.maveric.citi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.citi.enums.RoleEnum;
import com.maveric.citi.jwtuser.SigninRequest;
import com.maveric.citi.jwtuser.response.JwtAuthenticationTokenResponse;
import com.maveric.citi.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthencationControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private AuthenticationService authenticationService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
	}

	@Test
	public void testSignin_Success() throws Exception {

		SigninRequest request = new SigninRequest("subhra@gmail.com", "password");

		JwtAuthenticationTokenResponse response = new JwtAuthenticationTokenResponse("token", RoleEnum.SUPERUSER,1L);

		when(authenticationService.signin(request)).thenReturn(response);

		mockMvc.perform(post("/authenticate/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isOk());

		verify(authenticationService, times(1)).signin(request);
	}

}
