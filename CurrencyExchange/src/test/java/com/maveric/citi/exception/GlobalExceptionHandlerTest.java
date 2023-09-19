package com.maveric.citi.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    @Test
    void testHandleCustomerNotFoundException() {
        ResponseEntity<String> actualHandleCustomerNotFoundExceptionResult = globalExceptionHandler
                .handleCustomerNotFoundException(new Exception("foo"));
        assertEquals("foo", actualHandleCustomerNotFoundExceptionResult.getBody());
        assertEquals(404, actualHandleCustomerNotFoundExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleCustomerNotFoundExceptionResult.getHeaders().isEmpty());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testHandleCustomerNotFoundException2() {


        globalExceptionHandler.handleCustomerNotFoundException(null);
    }


    @Test
    void testHandleAccountalreadyExist() {
        ResponseEntity<Object> actualHandleAccountalreadyExistResult = globalExceptionHandler
                .handleAccountalreadyExist(new AccountAlreadyExistException("Ex"));
        assertEquals("Account Already Exists", actualHandleAccountalreadyExistResult.getBody());
        assertEquals(406, actualHandleAccountalreadyExistResult.getStatusCodeValue());
        assertTrue(actualHandleAccountalreadyExistResult.getHeaders().isEmpty());
    }


    @Test
    void testHandleAccountalreadyExist2() {
        ResponseEntity<Object> actualHandleAccountalreadyExistResult = globalExceptionHandler
                .handleAccountalreadyExist(mock(AccountAlreadyExistException.class));
        assertEquals("Account Already Exists", actualHandleAccountalreadyExistResult.getBody());
        assertEquals(406, actualHandleAccountalreadyExistResult.getStatusCodeValue());
        assertTrue(actualHandleAccountalreadyExistResult.getHeaders().isEmpty());
    }


    @Test
    void testHandleInsufficientBalanceExceptionEntity() {
        ResponseEntity<String> actualHandleInsufficientBalanceExceptionEntityResult = globalExceptionHandler
                .handleInsufficientBalanceExceptionEntity(new InsufficientBalanceException("Ex"));
        assertEquals("Ex", actualHandleInsufficientBalanceExceptionEntityResult.getBody());
        assertEquals(400, actualHandleInsufficientBalanceExceptionEntityResult.getStatusCodeValue());
        assertTrue(actualHandleInsufficientBalanceExceptionEntityResult.getHeaders().isEmpty());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testHandleInsufficientBalanceExceptionEntity2() {


        globalExceptionHandler.handleInsufficientBalanceExceptionEntity(null);
    }


    @Test
    void testHandleInsufficientBalanceExceptionEntity3() {
        InsufficientBalanceException e = mock(InsufficientBalanceException.class);
        when(e.getMessage()).thenReturn("Not all who wander are lost");
        ResponseEntity<String> actualHandleInsufficientBalanceExceptionEntityResult = globalExceptionHandler
                .handleInsufficientBalanceExceptionEntity(e);
        assertEquals("Not all who wander are lost", actualHandleInsufficientBalanceExceptionEntityResult.getBody());
        assertEquals(400, actualHandleInsufficientBalanceExceptionEntityResult.getStatusCodeValue());
        assertTrue(actualHandleInsufficientBalanceExceptionEntityResult.getHeaders().isEmpty());
        verify(e).getMessage();
    }


    @Test
    void testHandleInvalidRequestException() {
        ResponseEntity<String> actualHandleInvalidRequestExceptionResult = globalExceptionHandler
                .handleInvalidRequestException(new InvalidRequestException("Ex"));
        assertEquals("Ex", actualHandleInvalidRequestExceptionResult.getBody());
        assertEquals(400, actualHandleInvalidRequestExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleInvalidRequestExceptionResult.getHeaders().isEmpty());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testHandleInvalidRequestException2() {


        globalExceptionHandler.handleInvalidRequestException(null);
    }


    @Test
    void testHandleInvalidRequestException3() {
        InvalidRequestException e = mock(InvalidRequestException.class);
        when(e.getMessage()).thenReturn("Not all who wander are lost");
        ResponseEntity<String> actualHandleInvalidRequestExceptionResult = globalExceptionHandler
                .handleInvalidRequestException(e);
        assertEquals("Not all who wander are lost", actualHandleInvalidRequestExceptionResult.getBody());
        assertEquals(400, actualHandleInvalidRequestExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleInvalidRequestExceptionResult.getHeaders().isEmpty());
        verify(e).getMessage();
    }

    @Test
    void testAccounthandleNotFoundException() {
        ResponseEntity<String> actualAccounthandleNotFoundExceptionResult = globalExceptionHandler
                .AccounthandleNotFoundException(new Exception("foo"));
        assertEquals("foo", actualAccounthandleNotFoundExceptionResult.getBody());
        assertEquals(404, actualAccounthandleNotFoundExceptionResult.getStatusCodeValue());
        assertTrue(actualAccounthandleNotFoundExceptionResult.getHeaders().isEmpty());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testAccounthandleNotFoundException2() {


        globalExceptionHandler.AccounthandleNotFoundException(null);
    }


    @Test
    void testPropertyAlreadyExistException() {
        ResponseEntity<String> actualPropertyAlreadyExistExceptionResult = globalExceptionHandler
                .PropertyAlreadyExistException(new Exception("foo"));
        assertEquals("foo", actualPropertyAlreadyExistExceptionResult.getBody());
        assertEquals(406, actualPropertyAlreadyExistExceptionResult.getStatusCodeValue());
        assertTrue(actualPropertyAlreadyExistExceptionResult.getHeaders().isEmpty());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testPropertyAlreadyExistException2() {


        globalExceptionHandler.PropertyAlreadyExistException(null);
    }


    @Test
    void testHandleException() {
        ResponseEntity<String> actualHandleExceptionResult = globalExceptionHandler.handleException(new Exception("foo"));
        assertEquals("Bad Credentials!!", actualHandleExceptionResult.getBody());
        assertEquals(401, actualHandleExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleExceptionResult.getHeaders().isEmpty());
    }
}

