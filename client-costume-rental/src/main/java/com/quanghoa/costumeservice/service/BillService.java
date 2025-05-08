package com.quanghoa.costumeservice.service;

import com.quanghoa.costumeservice.model.BillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BillService {

    private final RestTemplate restTemplate;
    private final String apiUrl = "http://localhost:8083/api/bills"; // Adjust as needed

    @Autowired
    public BillService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> createBill(BillRequest billRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BillRequest> requestEntity = new HttpEntity<>(billRequest, headers);
        
        // Call the bills API endpoint
        return restTemplate.postForObject(apiUrl, requestEntity, Map.class);
    }
} 