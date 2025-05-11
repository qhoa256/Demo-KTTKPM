package com.costumeRental.billcostumeservice.service;

import com.costumeRental.billcostumeservice.exception.ResourceNotFoundException;
import com.costumeRental.billcostumeservice.exception.ServiceCommunicationException;
import com.costumeRental.billcostumeservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final RestTemplate restTemplate;
    
    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;
    
    @Autowired
    public CustomerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public Customer getCustomerById(Long id) {
        try {
            String url = userServiceUrl + "/api/customers/" + id;
            return restTemplate.getForObject(url, Customer.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        } catch (ResourceAccessException e) {
            throw new ServiceCommunicationException("Unable to connect to user-service", e);
        } catch (Exception e) {
            throw new ServiceCommunicationException("Error fetching customer data: " + e.getMessage(), e);
        }
    }
} 