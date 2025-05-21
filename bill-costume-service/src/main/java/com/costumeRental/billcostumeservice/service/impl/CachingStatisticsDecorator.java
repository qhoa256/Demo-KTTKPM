package com.costumeRental.billcostumeservice.service.impl;

import com.costumeRental.billcostumeservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Concrete decorator that adds caching functionality
 */
@Primary
@Component("cachingStatisticsDecorator")
public class CachingStatisticsDecorator extends StatisticsServiceDecorator {
    private final Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>();
    private final AtomicLong lastCacheUpdateTime = new AtomicLong(0);
    private static final long CACHE_TTL = 60 * 1000; // 1 minute cache TTL
    
    public CachingStatisticsDecorator(@Qualifier("loggingStatisticsDecorator") StatisticsService loggingStatisticsDecorator) {
        super(loggingStatisticsDecorator);
    }
    
    @Override
    public Map<String, Object> getRevenueByCategory() {
        String cacheKey = "revenueByCategory";
        long currentTime = System.currentTimeMillis();
        
        // Check if we have a valid cache entry
        if (cache.containsKey(cacheKey) && 
            (currentTime - lastCacheUpdateTime.get() < CACHE_TTL)) {
            return cache.get(cacheKey);
        }
        
        // Cache miss or expired, get fresh data
        Map<String, Object> result = super.getRevenueByCategory();
        
        // Update cache
        cache.put(cacheKey, result);
        lastCacheUpdateTime.set(currentTime);
        
        return result;
    }
    
    /**
     * Clear the cache
     */
    public void clearCache() {
        cache.clear();
        lastCacheUpdateTime.set(0);
    }
} 