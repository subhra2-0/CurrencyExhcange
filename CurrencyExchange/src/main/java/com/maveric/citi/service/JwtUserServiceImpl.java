package com.maveric.citi.service;


import com.maveric.citi.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtUserServiceImpl implements JwtUserService {
    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return customerRepository.findByEmail(email)
                        .orElseThrow(()->new UsernameNotFoundException("The User is not found"));




            }
        };
    }


}
