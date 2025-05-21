package com.quanghoa.costumeservice.service.impl;

import com.quanghoa.costumeservice.service.CostumeService;
import com.quanghoa.costumeservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Base implementation of the StatisticsService interface
 */
@Service("baseStatisticsService")
public class BaseStatisticsService implements StatisticsService {

    private final CostumeService costumeService;
    
    @Autowired
    public BaseStatisticsService(CostumeService costumeService) {
        this.costumeService = costumeService;
    }

    @Override
    public Map<String, Object> getRevenueByCategory() {
        return costumeService.getRevenueByCategory();
    }
}