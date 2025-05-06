package com.costumeRental.userservice.controller;

import com.costumeRental.userservice.model.Customer;
import com.costumeRental.userservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/rewards")
    public ResponseEntity<List<Customer>> getCustomersByRewardPoints(@RequestParam("minPoints") Integer minPoints) {
        return ResponseEntity.ok(customerService.getCustomersByMinRewardPoints(minPoints));
    }
    
    @GetMapping("/ranking/{ranking}")
    public ResponseEntity<List<Customer>> getCustomersByRanking(@PathVariable String ranking) {
        return ResponseEntity.ok(customerService.getCustomersByRanking(ranking));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Customer> loginCustomer(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok((Customer) customerService.login(username, password));
    }
} 