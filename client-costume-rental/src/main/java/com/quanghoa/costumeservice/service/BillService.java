package com.quanghoa.costumeservice.service;

import com.quanghoa.costumeservice.model.BillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class BillService {

    private final RestTemplate restTemplate;
    
    @Value("${bill.service.url:http://localhost:8083}")
    private String billServiceUrl;

    @Autowired
    public BillService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> createBill(Map<String, Object> billRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(billRequest, headers);
        
        // Call the bills API endpoint
        String apiUrl = billServiceUrl + "/api/bills";
        return restTemplate.postForObject(apiUrl, requestEntity, Map.class);
    }
    
    /**
     * Lấy danh sách các trang phục đang được thuê trong khoảng thời gian được chỉ định
     * 
     * @param rentDate Ngày khách hàng muốn thuê
     * @param returnDate Ngày khách hàng sẽ trả
     * @return Danh sách các trang phục đang được thuê và không khả dụng trong khoảng thời gian này
     */
    public List<Map<String, Object>> getRentedCostumes(String rentDate, String returnDate) {
        String apiUrl = billServiceUrl + "/api/bills/rented-costumes";
        
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("rentDate", rentDate)
                .queryParam("returnDate", returnDate);
            
            System.out.println("Gọi API kiểm tra trang phục đã thuê: " + builder.toUriString());
            
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            List<Map<String, Object>> result = response.getBody() != null ? response.getBody() : List.of();
            System.out.println("Tìm thấy " + result.size() + " trang phục đã được thuê trong khoảng thời gian này");
            return result;
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi API kiểm tra trang phục đã thuê: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Trả về danh sách rỗng trong trường hợp lỗi
        }
    }
} 