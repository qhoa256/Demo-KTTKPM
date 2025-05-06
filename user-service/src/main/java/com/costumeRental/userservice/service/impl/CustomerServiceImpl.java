package com.costumeRental.userservice.service.impl;

import com.costumeRental.userservice.dao.CustomerDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.Customer;
import com.costumeRental.userservice.model.FullName;
import com.costumeRental.userservice.model.User;
import com.costumeRental.userservice.service.CustomerService;
import com.costumeRental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;
    private final UserService userService;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = customerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        
        existingCustomer.setUsername(customerDetails.getUsername());
        existingCustomer.setPassword(customerDetails.getPassword());
        existingCustomer.setLoyaltyPoints(customerDetails.getLoyaltyPoints());
        
        if (customerDetails.getAddress() != null) {
            Address address = existingCustomer.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(existingCustomer);
                existingCustomer.setAddress(address);
            }
            address.setCity(customerDetails.getAddress().getCity());
            address.setStreet(customerDetails.getAddress().getStreet());
        }
        
        if (customerDetails.getFullName() != null) {
            FullName fullName = existingCustomer.getFullName();
            if (fullName == null) {
                fullName = new FullName();
                fullName.setUser(existingCustomer);
                existingCustomer.setFullName(fullName);
            }
            fullName.setFirstName(customerDetails.getFullName().getFirstName());
            fullName.setLastName(customerDetails.getFullName().getLastName());
        }
        
        return customerDao.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerDao.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        customerDao.deleteById(id);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer customer = customerDao.findByUsername(username);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with username: " + username);
        }
        return customer;
    }

    // DAO does not support these methods anymore
    @Override
    public List<Customer> getCustomersByMinRewardPoints(Integer minPoints) {
        throw new UnsupportedOperationException("Operation not supported with DAO implementation");
    }

    @Override
    public List<Customer> getCustomersByRanking(String ranking) {
        throw new UnsupportedOperationException("Operation not supported with DAO implementation");
    }

    // UserService methods delegation
    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userService.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public User login(String username, String password) {
        Customer customer = customerDao.findByUsername(username);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with username: " + username);
        }
        
        if (!customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return customer;
    }
} 