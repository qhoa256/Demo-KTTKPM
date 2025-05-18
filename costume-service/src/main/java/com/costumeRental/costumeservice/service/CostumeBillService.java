package com.costumeRental.costumeservice.service;

import com.costumeRental.costumeservice.model.CostumeBill;

import java.util.List;
import java.util.Map;

public interface CostumeBillService {
    CostumeBill createCostumeBill(CostumeBill costumeBill);
    CostumeBill getCostumeBillById(Long id);
    List<CostumeBill> getAllCostumeBills();
    List<CostumeBill> getCostumeBillsByBillId(String billId);
    List<CostumeBill> getCostumeBillsByCostumeId(Long costumeId);
    CostumeBill updateCostumeBill(Long id, CostumeBill costumeBill);
    void deleteCostumeBill(Long id);
    Map<String, Object> getRevenueByCategory();
} 