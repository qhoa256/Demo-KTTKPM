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
    public List<Map<String, Object>> getAllCostumes(String search, String category, String size, String price, String color) {
        StringBuilder apiUrl = new StringBuilder(costumeServiceUrl + "/api/costumes?");
        
        // Add filters to query parameters
        boolean hasParam = false;
        
        if (search != null && !search.isEmpty()) {
            apiUrl.append("search=").append(search);
            hasParam = true;
        }
        
        if (category != null && !category.isEmpty() && !category.equals("Tất cả")) {
            if (hasParam) apiUrl.append("&");
            apiUrl.append("category=").append(category);
            hasParam = true;
        }
        
        if (size != null && !size.isEmpty() && !size.equals("Tất cả")) {
            if (hasParam) apiUrl.append("&");
            apiUrl.append("size=").append(size);
            hasParam = true;
        }
        
        if (price != null && !price.isEmpty() && !price.equals("Tất cả")) {
            if (hasParam) apiUrl.append("&");
            apiUrl.append("price=").append(price);
            hasParam = true;
        }
        
        if (color != null && !color.isEmpty() && !color.equals("Tất cả")) {
            if (hasParam) apiUrl.append("&");
            apiUrl.append("color=").append(color);
        }
        
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                apiUrl.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        
        List<Map<String, Object>> costumes = response.getBody();
        
        return costumes != null ? costumes : List.of();
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

    @Override
    public List<Map<String, Object>> getAllCostumes() {
        return getAllCostumes(null, null, null, null, null);
    }
} 