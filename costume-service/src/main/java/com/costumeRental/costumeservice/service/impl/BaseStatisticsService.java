package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base implementation of the StatisticsService interface
 */
@Service("baseStatisticsService")
@RequiredArgsConstructor
public class BaseStatisticsService implements StatisticsService {

    private final CostumeBillDao costumeBillDao;
    private final CostumeDao costumeDao;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${bill-service.url:http://localhost:8083}")
    private String billServiceUrl;

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

    @Override
    public Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        // Get bills from bill service with date range
        List<Map<String, Object>> bills = getBillsFromBillService(startDate, endDate);

        // Debug logging
        System.out.println("=== DEBUG: getRevenueByCategoryWithDateRange ===");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Bills found: " + bills.size());
        if (!bills.isEmpty()) {
            System.out.println("First bill: " + bills.get(0));
        }

        // Get all costume bills
        List<CostumeBill> allCostumeBills = costumeBillDao.findAll();

        // Filter costume bills by bills that match the date range
        List<CostumeBill> filteredCostumeBills = allCostumeBills.stream()
                .filter(cb -> bills.stream().anyMatch(bill ->
                    bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId())))
                .collect(Collectors.toList());

        // Group by category and calculate revenue
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();

        for (CostumeBill bill : filteredCostumeBills) {
            if (bill.getCostume() != null && bill.getCostume().getCategory() != null) {
                String category = bill.getCostume().getCategory();
                BigDecimal revenue = bill.getRentPrice().multiply(BigDecimal.valueOf(bill.getQuantity()));

                categoryRevenue.put(category, categoryRevenue.getOrDefault(category, BigDecimal.ZERO).add(revenue));
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + bill.getQuantity());
            }
        }

        // Convert to list for pagination
        List<Map<String, Object>> categories = new ArrayList<>();
        for (String category : categoryRevenue.keySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", category);
            categoryData.put("revenue", categoryRevenue.get(category));
            categoryData.put("count", categoryCount.get(category));
            categories.add(categoryData);
        }

        // Apply pagination
        int totalElements = categories.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<Map<String, Object>> paginatedCategories = categories.subList(startIndex, endIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("content", paginatedCategories);
        result.put("totalElements", totalElements);
        result.put("totalPages", (int) Math.ceil((double) totalElements / size));
        result.put("currentPage", page);
        result.put("size", size);
        result.put("totalRevenue", categoryRevenue.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        return result;
    }

    @Override
    public Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size) {
        // Get bills from bill service with date range
        List<Map<String, Object>> bills = getBillsFromBillService(startDate, endDate);

        // Get costume bills for this category
        List<CostumeBill> allCostumeBills = costumeBillDao.findAll();

        // Filter by category and date range
        List<CostumeBill> filteredCostumeBills = allCostumeBills.stream()
                .filter(cb -> cb.getCostume() != null &&
                             category.equals(cb.getCostume().getCategory()) &&
                             bills.stream().anyMatch(bill ->
                                 bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId())))
                .collect(Collectors.toList());

        // Group by costume and calculate revenue
        Map<Long, Map<String, Object>> costumeData = new HashMap<>();

        for (CostumeBill cb : filteredCostumeBills) {
            Long costumeId = cb.getCostume().getId();

            if (!costumeData.containsKey(costumeId)) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", costumeId);
                data.put("name", cb.getCostume().getName());
                data.put("description", cb.getCostume().getDescription());
                data.put("price", cb.getCostume().getPrice());
                data.put("revenue", BigDecimal.ZERO);
                data.put("rentCount", 0);
                costumeData.put(costumeId, data);
            }

            Map<String, Object> data = costumeData.get(costumeId);
            BigDecimal currentRevenue = (BigDecimal) data.get("revenue");
            int currentCount = (Integer) data.get("rentCount");

            data.put("revenue", currentRevenue.add(cb.getRentPrice().multiply(BigDecimal.valueOf(cb.getQuantity()))));
            data.put("rentCount", currentCount + cb.getQuantity());
        }

        // Convert to list and apply pagination
        List<Map<String, Object>> costumes = new ArrayList<>(costumeData.values());
        int totalElements = costumes.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<Map<String, Object>> paginatedCostumes = costumes.subList(startIndex, endIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("content", paginatedCostumes);
        result.put("totalElements", totalElements);
        result.put("totalPages", (int) Math.ceil((double) totalElements / size));
        result.put("currentPage", page);
        result.put("size", size);

        return result;
    }

    @Override
    public Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size) {
        // Get bills from bill service with date range
        List<Map<String, Object>> bills = getBillsFromBillService(startDate, endDate);

        // Get costume bills for this costume
        List<CostumeBill> costumeBills = costumeBillDao.findAll().stream()
                .filter(cb -> cb.getCostume() != null && costumeId.equals(cb.getCostume().getId()))
                .collect(Collectors.toList());

        // Filter bills that contain this costume
        List<Map<String, Object>> relevantBills = bills.stream()
                .filter(bill -> costumeBills.stream()
                        .anyMatch(cb -> bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId())))
                .collect(Collectors.toList());

        // Apply pagination
        int totalElements = relevantBills.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<Map<String, Object>> paginatedBills = relevantBills.subList(startIndex, endIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("content", paginatedBills);
        result.put("totalElements", totalElements);
        result.put("totalPages", (int) Math.ceil((double) totalElements / size));
        result.put("currentPage", page);
        result.put("size", size);

        return result;
    }

    private List<Map<String, Object>> getBillsFromBillService(LocalDate startDate, LocalDate endDate) {
        try {
            String url = billServiceUrl + "/api/bills";
            System.out.println("=== DEBUG: Calling Bill Service ===");
            System.out.println("Bill Service URL: " + billServiceUrl);
            System.out.println("Full URL: " + url);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> allBills = restTemplate.getForObject(url, List.class);

            if (allBills == null) {
                return new ArrayList<>();
            }

            // Filter by date range if provided
            return allBills.stream()
                    .filter(bill -> {
                        if (startDate != null || endDate != null) {
                            Object rentDateObj = bill.get("rentDate");
                            if (rentDateObj != null) {
                                try {
                                    LocalDate rentDate = null;

                                    // Handle different date formats
                                    if (rentDateObj instanceof String) {
                                        String rentDateStr = (String) rentDateObj;
                                        rentDate = LocalDate.parse(rentDateStr);
                                    } else if (rentDateObj instanceof List) {
                                        // Handle array format [year, month, day]
                                        @SuppressWarnings("unchecked")
                                        List<Integer> dateArray = (List<Integer>) rentDateObj;
                                        if (dateArray.size() >= 3) {
                                            rentDate = LocalDate.of(dateArray.get(0), dateArray.get(1), dateArray.get(2));
                                        }
                                    } else if (rentDateObj instanceof Map) {
                                        // Handle object format {year: 2025, month: 4, day: 3}
                                        @SuppressWarnings("unchecked")
                                        Map<String, Object> dateMap = (Map<String, Object>) rentDateObj;
                                        Object year = dateMap.get("year");
                                        Object month = dateMap.get("month") != null ? dateMap.get("month") : dateMap.get("monthValue");
                                        Object day = dateMap.get("day") != null ? dateMap.get("day") : dateMap.get("dayOfMonth");

                                        if (year != null && month != null && day != null) {
                                            rentDate = LocalDate.of(
                                                Integer.parseInt(year.toString()),
                                                Integer.parseInt(month.toString()),
                                                Integer.parseInt(day.toString())
                                            );
                                        }
                                    }

                                    if (rentDate != null) {
                                        // Check date range
                                        if (startDate != null && rentDate.isBefore(startDate)) {
                                            return false;
                                        }
                                        if (endDate != null && rentDate.isAfter(endDate)) {
                                            return false;
                                        }
                                    } else {
                                        // If we can't parse the date, exclude it
                                        return false;
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error parsing date: " + rentDateObj + " - " + e.getMessage());
                                    return false;
                                }
                            } else {
                                // If no rent date, exclude it
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error calling bill service: " + e.getMessage());
            e.printStackTrace();
            // Return empty list if service is unavailable
            return new ArrayList<>();
        }
    }
}