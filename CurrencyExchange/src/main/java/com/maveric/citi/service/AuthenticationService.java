package com.maveric.citi.service;



import com.maveric.citi.jwtuser.SigninRequest;

import com.maveric.citi.jwtuser.response.JwtAuthenticationTokenResponse;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuthenticationService {
    JwtAuthenticationTokenResponse signin(SigninRequest request);



}
