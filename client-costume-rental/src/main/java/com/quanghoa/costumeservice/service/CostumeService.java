package com.quanghoa.costumeservice.service;

import java.util.List;
import java.util.Map;

public interface CostumeService {
    List<Map<String, Object>> getAllCostumes();
    List<Map<String, Object>> getAllCostumes(String search, String category, String size, String price, String color);
    List<Map<String, Object>> getCostumesBySupplierId(String supplierId);
    Map<String, Object> getRevenueByCategory();
    List<Map<String, Object>> getCostumeBillDetails(String costumeBillId);
} 