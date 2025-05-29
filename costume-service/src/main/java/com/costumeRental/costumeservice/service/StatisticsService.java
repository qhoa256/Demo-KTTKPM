package com.costumeRental.costumeservice.service;

import java.time.LocalDate;
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

    /**
     * Get revenue statistics by category with date range and pagination
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @return Map containing paginated revenue statistics data
     */
    Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size);

    /**
     * Get revenue statistics by category with advanced filtering
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @param timePeriod Time period type (all, monthly, yearly, custom)
     * @param minRevenue Minimum revenue filter
     * @param maxRevenue Maximum revenue filter
     * @return Map containing paginated revenue statistics data
     */
    Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size,
                                                         String timePeriod, Double minRevenue, Double maxRevenue);

    /**
     * Get costumes by category with date range and pagination
     * @param category Category name
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @return Map containing paginated costume data
     */
    Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size);

    /**
     * Get costumes by category with advanced filtering
     * @param category Category name
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @param timePeriod Time period type (all, monthly, yearly, custom)
     * @param minRevenue Minimum revenue filter
     * @param maxRevenue Maximum revenue filter
     * @return Map containing paginated costume data
     */
    Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size,
                                             String timePeriod, Double minRevenue, Double maxRevenue);

    /**
     * Get bills by costume with date range and pagination
     * @param costumeId Costume ID
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @return Map containing paginated bill data
     */
    Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size);

    /**
     * Get bills by costume with advanced filtering
     * @param costumeId Costume ID
     * @param startDate Start date for filtering
     * @param endDate End date for filtering
     * @param page Page number (0-based)
     * @param size Page size
     * @param timePeriod Time period type (all, monthly, yearly, custom)
     * @param minRevenue Minimum revenue filter
     * @param maxRevenue Maximum revenue filter
     * @return Map containing paginated bill data
     */
    Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size,
                                         String timePeriod, Double minRevenue, Double maxRevenue);
}