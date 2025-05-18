package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.CostumeBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostumeBillServiceImpl implements CostumeBillService {

    private final CostumeBillDao costumeBillDao;

    @Override
    public CostumeBill createCostumeBill(CostumeBill costumeBill) {
        return costumeBillDao.save(costumeBill);
    }

    @Override
    public CostumeBill getCostumeBillById(Long id) {
        return costumeBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));
    }

    @Override
    public List<CostumeBill> getAllCostumeBills() {
        return costumeBillDao.findAll();
    }

    @Override
    public List<CostumeBill> getCostumeBillsByBillId(String billId) {
        return costumeBillDao.findByBillId(billId);
    }

    @Override
    public List<CostumeBill> getCostumeBillsByCostumeId(Long costumeId) {
        return costumeBillDao.findByCostumeId(costumeId);
    }

    @Override
    public CostumeBill updateCostumeBill(Long id, CostumeBill costumeBillDetails) {
        CostumeBill existingCostumeBill = costumeBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));

        existingCostumeBill.setCostume(costumeBillDetails.getCostume());
        existingCostumeBill.setRentPrice(costumeBillDetails.getRentPrice());
        existingCostumeBill.setBillId(costumeBillDetails.getBillId());
        existingCostumeBill.setQuantity(costumeBillDetails.getQuantity());
        existingCostumeBill.setName(costumeBillDetails.getName());
        existingCostumeBill.setDescription(costumeBillDetails.getDescription());
        
        return costumeBillDao.save(existingCostumeBill);
    }

    @Override
    public void deleteCostumeBill(Long id) {
        if (!costumeBillDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("CostumeBill not found with id: " + id);
        }
        costumeBillDao.deleteById(id);
    }
    
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