package com.costumeRental.userservice.repository;

import com.costumeRental.userservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface CustomerRepository extends JpaRepository<Customer, Long> {
} 