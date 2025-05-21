package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of the StatisticsService interface
 */
@Service("baseStatisticsService")
@RequiredArgsConstructor
public class BaseStatisticsService implements StatisticsService {

    private final CostumeBillDao costumeBillDao;

    @Override
    public Map<String, Object> getRevenueByCategory() {
        List<CostumeBill> allCostumeBills = costumeBillDao.findAll();
        
        // Group costume bills by category and calculate revenue
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, List<String>> categoryBillIds = new HashMap<>();
        
        for (CostumeBill bill : allCostumeBills) {
            if (bill.getCostume() != null && bill.getCostume().getCategory() != null) {
                String category = bill.getCostume().getCategory();
                BigDecimal revenue = bill.getRentPrice().multiply(BigDecimal.valueOf(bill.getQuantity()));
                
                // Update revenue for category
                categoryRevenue.put(
                    category, 
                    categoryRevenue.getOrDefault(category, BigDecimal.ZERO).add(revenue)
                );
                
                // Update count for category
                categoryCount.put(
                    category,
                    categoryCount.getOrDefault(category, 0) + bill.getQuantity()
                );
                
                // Add bill ID to the list of bill IDs for this category
                if (bill.getBillId() != null) {
                    List<String> billIds = categoryBillIds.getOrDefault(category, new ArrayList<>());
                    if (!billIds.contains(bill.getBillId())) {
                        billIds.add(bill.getBillId());
                    }
                    categoryBillIds.put(category, billIds);
                }
            }
        }
        
        // Prepare result in format expected by client
        List<Map<String, Object>> categories = new ArrayList<>();
        for (String category : categoryRevenue.keySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", category);
            categoryData.put("revenue", categoryRevenue.get(category));
            categoryData.put("count", categoryCount.get(category));
            categoryData.put("billIds", categoryBillIds.getOrDefault(category, new ArrayList<>()));
            categories.add(categoryData);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("categories", categories);
        result.put("totalRevenue", categoryRevenue.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        return result;
    }
} 