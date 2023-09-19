package com.maveric.citi.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.maveric.citi.entities.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long>
{
	Optional<Customer> findByEmail(String email);

	Optional<Customer> findBycustomerName(String username);


	
}
