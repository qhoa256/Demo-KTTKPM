package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.service.StatisticsService;

import java.time.LocalDate;
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

    @Override
    public Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        return decoratedService.getRevenueByCategoryWithDateRange(startDate, endDate, page, size);
    }

    @Override
    public Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size,
                                                               String timePeriod, Double minRevenue, Double maxRevenue) {
        return decoratedService.getRevenueByCategoryWithDateRange(startDate, endDate, page, size, timePeriod, minRevenue, maxRevenue);
    }

    @Override
    public Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size) {
        return decoratedService.getCostumesByCategory(category, startDate, endDate, page, size);
    }

    @Override
    public Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size,
                                                    String timePeriod, Double minRevenue, Double maxRevenue) {
        return decoratedService.getCostumesByCategory(category, startDate, endDate, page, size, timePeriod, minRevenue, maxRevenue);
    }

    @Override
    public Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size) {
        return decoratedService.getBillsByCostume(costumeId, startDate, endDate, page, size);
    }

    @Override
    public Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size,
                                                String timePeriod, Double minRevenue, Double maxRevenue) {
        return decoratedService.getBillsByCostume(costumeId, startDate, endDate, page, size, timePeriod, minRevenue, maxRevenue);
    }
}