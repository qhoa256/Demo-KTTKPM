package com.costumeRental.userservice.service;

import com.costumeRental.userservice.model.Customer;

import java.util.List;

public interface CustomerService extends UserService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
    Customer getCustomerByUsername(String username);
    List<Customer> getCustomersByMinRewardPoints(Integer minPoints);
    List<Customer> getCustomersByRanking(String ranking);
} 