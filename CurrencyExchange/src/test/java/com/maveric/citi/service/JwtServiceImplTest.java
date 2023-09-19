package com.maveric.citi.service;

import static org.mockito.Mockito.mock;

import com.maveric.citi.entities.Account;
import com.maveric.citi.entities.CurrencyConversion;
import com.maveric.citi.entities.Customer;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JwtServiceImpl.class})
@ExtendWith(SpringExtension.class)
class JwtServiceImplTest {
    @Autowired
    private JwtServiceImpl jwtServiceImpl;

    @Test
    @Disabled("TODO: Complete this test")
    void testExtractUserName() {


        jwtServiceImpl.extractUserName("ABC123");
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testExtractUserName2() {


        jwtServiceImpl.extractUserName(JwtServiceImpl.SECRET);
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testExtractUserName3() {


        jwtServiceImpl.extractUserName("");
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testGenerateToken() {


        jwtServiceImpl.generateToken(new Customer());
    }

    @Test
    @Disabled("TODO: Complete this test")
    void testGenerateToken2() {


        ArrayList<Account> account = new ArrayList<>();
        ArrayList<CurrencyConversion> currencyConversions = new ArrayList<>();
        jwtServiceImpl.generateToken(
                new Customer(1L, account, currencyConversions, JwtServiceImpl.SECRET, "iloveyou", GenderEnum.MALE,
                        "jane.doe@example.org", 1800000L, LocalDate.of(1970, 1, 1).atStartOfDay(), RoleEnum.SUPERUSER));
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testGenerateToken3() {


        jwtServiceImpl.generateToken(null);
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testIsTokenValid() {


        jwtServiceImpl.isTokenValid("ABC123", new Customer());
    }

    @Test
    @Disabled("TODO: Complete this test")
    void testIsTokenValid2() {


        jwtServiceImpl.isTokenValid("", new Customer());
    }

    @Test
    @Disabled("TODO: Complete this test")
    void testIsTokenValid3() {


        jwtServiceImpl.isTokenValid(JwtServiceImpl.SECRET, mock(Customer.class));
    }
}

