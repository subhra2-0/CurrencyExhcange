package com.maveric.citi.service;

import com.maveric.citi.repository.ICustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JwtUserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class JwtUserServiceImplTest {
    @MockBean
    private ICustomerRepository iCustomerRepository;

    @Autowired
    private JwtUserServiceImpl jwtUserServiceImpl;


    @Test
    void testUserDetailsService() {

        jwtUserServiceImpl.userDetailsService();
    }
}

