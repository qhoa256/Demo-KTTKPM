package com.quanghoa.costumeservice.service;

import com.quanghoa.costumeservice.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportBillService {

    private final RestTemplate restTemplate;
    
    @Value("${services.import-bill-service.url}")
    private String importBillServiceUrl;
    
    @Autowired
    public ImportBillService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Prepare the import bill request payload
     */
    public Map<String, Object> prepareImportBillRequest(UserResponse userData, Map<String, Object> importData) {
        Map<String, Object> requestPayload = new HashMap<>();
        
        // Set manager info from userData
        Map<String, Object> manager = new HashMap<>();
        manager.put("id", userData.getId());
        manager.put("type", "staff");
        manager.put("username", userData.getUsername());
        manager.put("position", "Manager");
        manager.put("fullName", userData.getFullName());
        manager.put("address", userData.getAddress());
        requestPayload.put("manager", manager);
        
        // Set supplier info from importData
        requestPayload.put("supplier", importData.get("supplier"));
        
        // Set import date
        requestPayload.put("importDate", importData.get("importDate"));
        
        // Set costume importing bills
        List<Map<String, Object>> costumeImportingBills = new ArrayList<>();
        
        // Process each costume item from the form
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> costumes = (List<Map<String, Object>>) importData.get("costumes");
        
        for (Map<String, Object> costume : costumes) {
            Map<String, Object> costumeImportingBill = new HashMap<>();
            
            // Create costumeSupplier object
            Map<String, Object> costumeSupplier = new HashMap<>();
            costumeSupplier.put("id", costume.get("costumeSupplierID"));
            
            // Create costume object
            Map<String, Object> costumeObj = new HashMap<>();
            costumeObj.put("id", costume.get("id"));
            costumeObj.put("category", costume.get("category"));
            costumeObj.put("name", costume.get("name"));
            costumeObj.put("description", costume.get("description") != null ? costume.get("description") : "");
            costumeObj.put("price", costume.get("importPrice"));
            
            // Create supplier object
            Map<String, Object> supplierObj = (Map<String, Object>) importData.get("supplier");
            
            // Add costume and supplier to costumeSupplier
            costumeSupplier.put("costume", costumeObj);
            costumeSupplier.put("supplier", supplierObj);
            
            // Add all properties to costume importing bill
            costumeImportingBill.put("costumeSupplier", costumeSupplier);
            costumeImportingBill.put("importPrice", costume.get("importPrice"));
            costumeImportingBill.put("quantity", costume.get("quantity"));
            costumeImportingBill.put("name", costume.get("name"));
            costumeImportingBill.put("description", costume.get("description") != null ? costume.get("description") : "");
            
            // Add to costume importing bills list
            costumeImportingBills.add(costumeImportingBill);
        }
        
        requestPayload.put("costumeImportingBills", costumeImportingBills);
        
        return requestPayload;
    }
    
    /**
     * Send the request to import-bill-service API
     */
    public Map<String, Object> createImportBill(Map<String, Object> requestPayload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);
        
        return restTemplate.postForObject(
                importBillServiceUrl + "/api/importing-bills",
                request,
                Map.class
        );
    }
} 