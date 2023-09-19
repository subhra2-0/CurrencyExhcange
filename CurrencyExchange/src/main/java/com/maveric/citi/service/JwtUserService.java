package com.maveric.citi.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface JwtUserService {
    UserDetailsService userDetailsService();
}
