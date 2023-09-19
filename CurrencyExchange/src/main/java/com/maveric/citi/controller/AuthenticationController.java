package com.maveric.citi.controller;




import com.maveric.citi.jwtuser.SigninRequest;
import com.maveric.citi.jwtuser.response.JwtAuthenticationTokenResponse;
import com.maveric.citi.service.AuthenticationService;
import com.maveric.citi.service.JwtService;
import com.maveric.citi.service.JwtUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtUserServiceImpl jwtUserServiceImpl;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationTokenResponse> signin(@RequestBody SigninRequest request) {
        System.out.println("inside Auth COntroller, Sign In Requested");
        return ResponseEntity.ok(authenticationService.signin(request));
    }





    }








