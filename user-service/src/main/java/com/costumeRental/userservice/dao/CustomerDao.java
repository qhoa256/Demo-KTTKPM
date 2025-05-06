package com.costumeRental.userservice.dao;

import com.costumeRental.userservice.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Customer findByUsername(String username);
    List<Customer> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
} 