package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.CostumeBillService;
import com.costumeRental.costumeservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CostumeBillServiceImpl implements CostumeBillService {

    private final CostumeBillDao costumeBillDao;
    private final StatisticsService statisticsService;

    @Autowired
    public CostumeBillServiceImpl(CostumeBillDao costumeBillDao, 
                                @Qualifier("cachingStatisticsDecorator") StatisticsService statisticsService) {
        this.costumeBillDao = costumeBillDao;
        this.statisticsService = statisticsService;
    }

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
        // Delegate to the decorated statistics service
        return statisticsService.getRevenueByCategory();
    }
} 