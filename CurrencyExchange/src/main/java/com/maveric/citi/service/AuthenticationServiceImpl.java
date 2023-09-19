package com.maveric.citi.service;
import com.maveric.citi.jwtuser.SigninRequest;
import com.maveric.citi.jwtuser.response.JwtAuthenticationTokenResponse;
import com.maveric.citi.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper mapper;



    @Override
    public JwtAuthenticationTokenResponse signin(SigninRequest request) {

        logger.info("requesstttt "+request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationTokenResponse.builder().token(jwt)
                .role(user.getRole())
                .id(user.getCustomerId())
                .build();
    }


}

