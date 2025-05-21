package com.quanghoa.costumeservice.service;

import java.util.Map;

/**
 * Interface for statistics services using Decorator pattern
 */
public interface StatisticsService {
    /**
     * Get revenue statistics by category
     * @return Map containing revenue statistics data
     */
    Map<String, Object> getRevenueByCategory();
} 