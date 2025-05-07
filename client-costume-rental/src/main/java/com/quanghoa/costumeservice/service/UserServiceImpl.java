package com.quanghoa.costumeservice.service;

import com.quanghoa.costumeservice.model.LoginRequest;
import com.quanghoa.costumeservice.model.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;
    
    @Value("${user.service.url}")
    private String userServiceUrl;
    
    public UserServiceImpl() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public UserResponse login(LoginRequest loginRequest) {
        String loginEndpoint = userServiceUrl + "/api/users/login";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        
        try {
            ResponseEntity<UserResponse> response = restTemplate.postForEntity(
                    loginEndpoint,
                    request,
                    UserResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            // Handle exceptions - return null since we can't create a UserResponse with error message
            return null;
        }
    }
} 