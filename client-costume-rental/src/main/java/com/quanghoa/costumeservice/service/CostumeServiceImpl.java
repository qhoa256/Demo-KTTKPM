package com.quanghoa.costumeservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CostumeServiceImpl implements CostumeService {
    
    private final RestTemplate restTemplate;
    
    @Value("${costume.service.url:http://localhost:8080}")
    private String costumeServiceUrl;
    
    public CostumeServiceImpl() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public List<Map<String, Object>> getAllCostumes() {
        String apiUrl = costumeServiceUrl + "/api/costumes";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }
    
    @Override
    public List<Map<String, Object>> getCostumesBySupplierId(String supplierId) {
        String apiUrl = costumeServiceUrl + "/api/costume-suppliers/supplier/" + supplierId;
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        
        return response.getBody() != null ? response.getBody() : List.of();
    }
} 