package com.costumeRental.billcostumeservice.service.impl;

import com.costumeRental.billcostumeservice.service.StatisticsService;

import java.util.Map;

/**
 * Abstract decorator for StatisticsService
 */
public abstract class StatisticsServiceDecorator implements StatisticsService {
    protected final StatisticsService decoratedService;
    
    public StatisticsServiceDecorator(StatisticsService decoratedService) {
        this.decoratedService = decoratedService;
    }
    
    @Override
    public Map<String, Object> getRevenueByCategory() {
        return decoratedService.getRevenueByCategory();
    }
} 