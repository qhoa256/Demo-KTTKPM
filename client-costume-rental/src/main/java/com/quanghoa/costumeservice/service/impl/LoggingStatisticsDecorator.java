package com.quanghoa.costumeservice.service.impl;

import com.quanghoa.costumeservice.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Concrete decorator that adds logging functionality
 */
@Component("loggingStatisticsDecorator")
public class LoggingStatisticsDecorator extends StatisticsServiceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(LoggingStatisticsDecorator.class);
    
    public LoggingStatisticsDecorator(@Qualifier("baseStatisticsService") StatisticsService baseStatisticsService) {
        super(baseStatisticsService);
    }
    
    @Override
    public Map<String, Object> getRevenueByCategory() {
        logger.info("Getting revenue statistics by category");
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> result = super.getRevenueByCategory();
        
        long endTime = System.currentTimeMillis();
        logger.info("Revenue statistics by category retrieved in {} ms", endTime - startTime);
        
        if (result.containsKey("totalRevenue")) {
            logger.info("Total revenue: {}", result.get("totalRevenue"));
        }
        
        return result;
    }
} 