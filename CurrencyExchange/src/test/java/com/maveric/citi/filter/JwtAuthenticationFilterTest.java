package com.maveric.citi.filter;

import com.maveric.citi.service.JwtService;
import com.maveric.citi.service.JwtUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.catalina.connector.Response;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {JwtAuthenticationFilter.class})
@WebAppConfiguration
@ExtendWith(SpringExtension.class)

class JwtAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtUserService jwtUserService;


    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testDoFilterInternal2() throws ServletException, IOException {


        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        jwtAuthenticationFilter.doFilterInternal(null, response, filterChain);
    }


    @Test
    void testDoFilterInternal3() throws ServletException, IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(request).getHeader(Mockito.<String>any());
        verify(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testDoFilterInternal4() throws ServletException, IOException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.jdbc.core.JdbcTemplate.query(String, org.springframework.jdbc.core.RowMapper, Object[])" because the return value of "org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl.getJdbcTemplate()" is null
        //       at com.maveric.citi.filter.JwtAuthenticationFilter.doFilterInternal(JwtAuthenticationFilter.java:49)
        //   See https://diff.blue/R013 to resolve this issue.

        when(jwtService.extractUserName(Mockito.<String>any())).thenReturn("janedoe");
        when(jwtUserService.userDetailsService()).thenReturn(new JdbcDaoImpl());
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
    }


    @Test
    void testDoFilterInternal5() throws ServletException, IOException {
        when(jwtService.extractUserName(Mockito.<String>any())).thenReturn(null);
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(jwtService).extractUserName(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
    }


}