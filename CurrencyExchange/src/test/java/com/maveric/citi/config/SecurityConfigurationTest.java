package com.maveric.citi.config;

import com.maveric.citi.filter.JwtAuthenticationFilter;
import com.maveric.citi.service.JwtUserService;

import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {SecurityConfiguration.class})
@ExtendWith(SpringExtension.class)

class SecurityConfigurationTest {

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private HttpSecurity httpSecurity;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUserService jwtUserService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @MockBean
    private SecurityFilterChain securityFilterChain;


    @Test
    @Disabled("TODO: Complete this test")
    void testSecurityFilterChain() throws Exception {


        AuthenticationManagerBuilder authenticationBuilder = new AuthenticationManagerBuilder(null);
        securityConfiguration.securityFilterChain(new HttpSecurity(null, authenticationBuilder, new HashMap<>()));
    }


    @Test
    void testCorsOrginConfig() {
        assertTrue(securityConfiguration.corsOrginConfig() instanceof UrlBasedCorsConfigurationSource);
    }


    @Test
    void testPasswordEncoder() {
        assertTrue(securityConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }


    @Test
    void testAuthenticationProvider() {


        securityConfiguration.authenticationProvider();
    }


    @Test
    void testAuthenticationManager() throws Exception {
        assertTrue(
                securityConfiguration.authenticationManager(new AuthenticationConfiguration()) instanceof ProviderManager);
    }

}