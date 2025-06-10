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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        // Get all bills from bill service (no date filter)
        List<Map<String, Object>> bills = getBillsFromBillService(null, null);

        // Get all costume bills
        List<CostumeBill> allCostumeBills = costumeBillDao.findAll();

        // Group costume bills by category and calculate revenue using new logic
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, List<String>> categoryBillIds = new HashMap<>();

        for (CostumeBill costumeBill : allCostumeBills) {
            if (costumeBill.getCostume() != null && costumeBill.getCostume().getCategory() != null) {
                String category = costumeBill.getCostume().getCategory();

                // Find the corresponding bill to get rental dates
                Map<String, Object> correspondingBill = bills.stream()
                    .filter(bill -> bill.get("id") != null && bill.get("id").toString().equals(costumeBill.getBillId()))
                    .findFirst()
                    .orElse(null);

                BigDecimal revenue;
                if (correspondingBill != null) {
                    // Calculate revenue using new logic (no date filter, so use full rental period)
                    revenue = calculateProportionalRevenue(costumeBill, correspondingBill, null, null);
                } else {
                    // Fallback: giả sử thuê 1 ngày nếu không tìm thấy hóa đơn
                    revenue = costumeBill.getRentPrice().multiply(BigDecimal.valueOf(costumeBill.getQuantity()));
                }

                // Update revenue for category
                categoryRevenue.put(
                    category,
                    categoryRevenue.getOrDefault(category, BigDecimal.ZERO).add(revenue)
                );

                // Update count for category
                categoryCount.put(
                    category,
                    categoryCount.getOrDefault(category, 0) + costumeBill.getQuantity()
                );

                // Add bill ID to the list of bill IDs for this category
                if (costumeBill.getBillId() != null) {
                    List<String> billIds = categoryBillIds.getOrDefault(category, new ArrayList<>());
                    if (!billIds.contains(costumeBill.getBillId())) {
                        billIds.add(costumeBill.getBillId());
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
        return getRevenueByCategoryWithDateRange(startDate, endDate, page, size, "custom", null, null);
    }

    @Override
    public Map<String, Object> getRevenueByCategoryWithDateRange(LocalDate startDate, LocalDate endDate, int page, int size,
                                                               String timePeriod, Double minRevenue, Double maxRevenue) {
        // Get bills from bill service with date range
        List<Map<String, Object>> bills = getBillsFromBillService(startDate, endDate);

        // Debug logging
        System.out.println("=== DEBUG: getRevenueByCategoryWithDateRange (Advanced) ===");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Time Period: " + timePeriod);
        System.out.println("Revenue Filter: " + minRevenue + " - " + maxRevenue);
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

        // Group by category and calculate proportional revenue
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();

        for (CostumeBill costumeBill : filteredCostumeBills) {
            if (costumeBill.getCostume() != null && costumeBill.getCostume().getCategory() != null) {
                String category = costumeBill.getCostume().getCategory();

                // Find the corresponding bill to get rental dates
                Map<String, Object> correspondingBill = bills.stream()
                    .filter(bill -> bill.get("id") != null && bill.get("id").toString().equals(costumeBill.getBillId()))
                    .findFirst()
                    .orElse(null);

                BigDecimal revenue;
                if (correspondingBill != null) {
                    // Calculate proportional revenue based on actual rental period within filter range
                    revenue = calculateProportionalRevenue(costumeBill, correspondingBill, startDate, endDate);
                } else {
                    // Fallback: giả sử thuê 1 ngày nếu không tìm thấy hóa đơn
                    revenue = costumeBill.getRentPrice().multiply(BigDecimal.valueOf(costumeBill.getQuantity()));
                }

                categoryRevenue.put(category, categoryRevenue.getOrDefault(category, BigDecimal.ZERO).add(revenue));
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + costumeBill.getQuantity());
            }
        }

        // Convert to list and apply filters
        List<Map<String, Object>> categories = new ArrayList<>();
        for (String category : categoryRevenue.keySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", category);
            categoryData.put("revenue", categoryRevenue.get(category));
            categoryData.put("count", categoryCount.get(category));

            // Apply revenue filters
            BigDecimal revenue = categoryRevenue.get(category);
            if (minRevenue != null && revenue.doubleValue() < minRevenue) {
                continue;
            }
            if (maxRevenue != null && revenue.doubleValue() > maxRevenue) {
                continue;
            }



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
        result.put("totalRevenue", categories.stream()
                .map(cat -> (BigDecimal) cat.get("revenue"))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return result;
    }

    @Override
    public Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size) {
        return getCostumesByCategory(category, startDate, endDate, page, size, "custom", null, null);
    }

    @Override
    public Map<String, Object> getCostumesByCategory(String category, LocalDate startDate, LocalDate endDate, int page, int size,
                                                    String timePeriod, Double minRevenue, Double maxRevenue) {
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

        // Group by costume and calculate proportional revenue
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

            // Find the corresponding bill to get rental dates
            Map<String, Object> correspondingBill = bills.stream()
                .filter(bill -> bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId()))
                .findFirst()
                .orElse(null);

            BigDecimal revenue;
            if (correspondingBill != null) {
                // Calculate proportional revenue based on actual rental period within filter range
                revenue = calculateProportionalRevenue(cb, correspondingBill, startDate, endDate);
            } else {
                // Fallback: giả sử thuê 1 ngày nếu không tìm thấy hóa đơn
                revenue = cb.getRentPrice().multiply(BigDecimal.valueOf(cb.getQuantity()));
            }

            data.put("revenue", currentRevenue.add(revenue));
            data.put("rentCount", currentCount + cb.getQuantity());
        }

        // Convert to list and apply filters
        List<Map<String, Object>> costumes = new ArrayList<>();
        for (Map<String, Object> costumeInfo : costumeData.values()) {
            // Apply revenue filters
            BigDecimal revenue = (BigDecimal) costumeInfo.get("revenue");
            if (minRevenue != null && revenue.doubleValue() < minRevenue) {
                continue;
            }
            if (maxRevenue != null && revenue.doubleValue() > maxRevenue) {
                continue;
            }



            costumes.add(costumeInfo);
        }

        // Apply pagination
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
        return getBillsByCostume(costumeId, startDate, endDate, page, size, "custom", null, null);
    }

    @Override
    public Map<String, Object> getBillsByCostume(Long costumeId, LocalDate startDate, LocalDate endDate, int page, int size,
                                                String timePeriod, Double minRevenue, Double maxRevenue) {
        // Get bills from bill service with date range
        List<Map<String, Object>> bills = getBillsFromBillService(startDate, endDate);

        // Get costume bills for this costume
        List<CostumeBill> costumeBills = costumeBillDao.findAll().stream()
                .filter(cb -> cb.getCostume() != null && costumeId.equals(cb.getCostume().getId()))
                .collect(Collectors.toList());

        // Filter bills that contain this costume and apply filters
        List<Map<String, Object>> relevantBills = bills.stream()
                .filter(bill -> {
                    // Check if bill contains this costume
                    boolean containsCostume = costumeBills.stream()
                            .anyMatch(cb -> bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId()));

                    if (!containsCostume) {
                        return false;
                    }

                    // Calculate proportional bill revenue and order count for this costume
                    BigDecimal billRevenue = BigDecimal.ZERO;
                    int billOrderCount = 0;

                    for (CostumeBill cb : costumeBills) {
                        if (bill.get("id") != null && bill.get("id").toString().equals(cb.getBillId())) {
                            // Calculate proportional revenue based on actual rental period within filter range
                            BigDecimal revenue = calculateProportionalRevenue(cb, bill, startDate, endDate);
                            billRevenue = billRevenue.add(revenue);
                            billOrderCount += cb.getQuantity();
                        }
                    }

                    // Apply revenue filters
                    if (minRevenue != null && billRevenue.doubleValue() < minRevenue) {
                        return false;
                    }
                    if (maxRevenue != null && billRevenue.doubleValue() > maxRevenue) {
                        return false;
                    }



                    return true;
                })
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

    /**
     * Lấy danh sách hóa đơn từ Bill Service với logic filter mới
     * Logic mới: Lọc các hóa đơn có khoảng thời gian thuê overlap với khoảng thời gian filter
     * Thay vì chỉ filter theo ngày bắt đầu thuê như trước
     */
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

            // Lọc theo khoảng thời gian - chỉ lấy các hóa đơn có khoảng thời gian thuê overlap với khoảng filter
            return allBills.stream()
                    .filter(bill -> {
                        if (startDate != null || endDate != null) {
                            Object rentDateObj = bill.get("rentDate");      // Ngày bắt đầu thuê
                            Object returnDateObj = bill.get("returnDate");  // Ngày trả trang phục

                            if (rentDateObj != null) {
                                try {
                                    LocalDate rentDate = parseDateObject(rentDateObj);
                                    LocalDate returnDate = returnDateObj != null ? parseDateObject(returnDateObj) : null;

                                    if (rentDate != null) {
                                        // Kiểm tra xem khoảng thời gian thuê có overlap với khoảng filter không

                                        // Điều kiện 1: Ngày bắt đầu thuê phải trước hoặc bằng ngày kết thúc filter
                                        boolean startsBeforeEnd = endDate == null || !rentDate.isAfter(endDate);

                                        // Điều kiện 2: Ngày trả phải sau hoặc bằng ngày bắt đầu filter
                                        // (hoặc chưa trả - returnDate = null)
                                        boolean endsAfterStart = startDate == null || returnDate == null || !returnDate.isBefore(startDate);

                                        // Chỉ lấy hóa đơn thỏa mãn cả 2 điều kiện (có overlap)
                                        return startsBeforeEnd && endsAfterStart;
                                    } else {
                                        // Nếu không parse được ngày thuê, loại bỏ hóa đơn này
                                        return false;
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error parsing date: " + rentDateObj + " - " + e.getMessage());
                                    return false;
                                }
                            } else {
                                // Nếu không có ngày thuê, loại bỏ hóa đơn này
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error calling bill service: " + e.getMessage());
            e.printStackTrace();
            // Trả về danh sách rỗng nếu service không khả dụng
            return new ArrayList<>();
        }
    }

    /**
     * Method hỗ trợ để parse các object ngày tháng từ nhiều format khác nhau
     * Xử lý được: String, List [year, month, day], Map {year, month, day}
     */
    private LocalDate parseDateObject(Object dateObj) {
        if (dateObj == null) {
            return null;
        }

        try {
            if (dateObj instanceof String) {
                // Format: "2025-01-15"
                String dateStr = (String) dateObj;
                return LocalDate.parse(dateStr);
            } else if (dateObj instanceof List) {
                // Format: [2025, 1, 15] - array format từ JSON
                @SuppressWarnings("unchecked")
                List<Integer> dateArray = (List<Integer>) dateObj;
                if (dateArray.size() >= 3) {
                    return LocalDate.of(dateArray.get(0), dateArray.get(1), dateArray.get(2));
                }
            } else if (dateObj instanceof Map) {
                // Format: {year: 2025, month: 1, day: 15} - object format từ JSON
                @SuppressWarnings("unchecked")
                Map<String, Object> dateMap = (Map<String, Object>) dateObj;
                Object year = dateMap.get("year");
                // Xử lý cả "month" và "monthValue" để tương thích với các API khác nhau
                Object month = dateMap.get("month") != null ? dateMap.get("month") : dateMap.get("monthValue");
                // Xử lý cả "day" và "dayOfMonth" để tương thích với các API khác nhau
                Object day = dateMap.get("day") != null ? dateMap.get("day") : dateMap.get("dayOfMonth");

                if (year != null && month != null && day != null) {
                    return LocalDate.of(
                        Integer.parseInt(year.toString()),
                        Integer.parseInt(month.toString()),
                        Integer.parseInt(day.toString())
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date object: " + dateObj + " - " + e.getMessage());
        }

        return null;
    }

    /**
     * Tính revenue theo tỷ lệ dựa trên khoảng thời gian thực tế sử dụng trong filter range
     *
     * Logic đã sửa (costumeBill.getRentPrice() là giá thuê MỘT NGÀY):
     * 1. Nếu không có filter (filterStartDate và filterEndDate đều null): tính toàn bộ thời gian thuê
     * 2. Nếu có filter: Tìm khoảng thời gian overlap giữa rental period và filter range
     * 3. Revenue = rentPrice (giá/ngày) × số ngày × quantity
     *
     * Ví dụ: Thuê từ 01/01 đến 10/01, giá 100,000 VND/ngày
     *        Filter từ 05/01 đến 15/01
     *        => Overlap: 05/01 đến 10/01 (6 ngày)
     *        => Revenue: 100,000 × 6 × quantity = 600,000 VND
     */
    private BigDecimal calculateProportionalRevenue(CostumeBill costumeBill, Map<String, Object> bill,
                                                   LocalDate filterStartDate, LocalDate filterEndDate) {
        try {
            // Lấy thông tin ngày thuê và ngày trả từ hóa đơn
            LocalDate rentDate = parseDateObject(bill.get("rentDate"));      // Ngày bắt đầu thuê
            LocalDate returnDate = parseDateObject(bill.get("returnDate"));  // Ngày trả trang phục

            if (rentDate == null) {
                return BigDecimal.ZERO;
            }

            // Nếu chưa trả (returnDate = null), sử dụng ngày kết thúc filter hoặc ngày hiện tại
            if (returnDate == null) {
                returnDate = filterEndDate != null ? filterEndDate : LocalDate.now();
            }

            // Nếu không có filter ngày tháng, tính toàn bộ thời gian thuê
            if (filterStartDate == null && filterEndDate == null) {
                long totalRentalDays = ChronoUnit.DAYS.between(rentDate, returnDate) + 1;
                if (totalRentalDays <= 0) {
                    return BigDecimal.ZERO;
                }

                BigDecimal totalRevenue = costumeBill.getRentPrice()
                                                    .multiply(BigDecimal.valueOf(totalRentalDays))
                                                    .multiply(BigDecimal.valueOf(costumeBill.getQuantity()));

                System.out.println("=== DEBUG: Tính toàn bộ thời gian thuê (Không filter) ===");
                System.out.println("Trang phục: " + costumeBill.getCostume().getName());
                System.out.println("Ngày thuê: " + rentDate + ", Ngày trả: " + returnDate);
                System.out.println("Tổng số ngày thuê: " + totalRentalDays);
                System.out.println("Giá thuê/ngày: " + costumeBill.getRentPrice() + ", Số lượng: " + costumeBill.getQuantity());
                System.out.println("Tổng revenue: " + totalRevenue);

                return totalRevenue;
            }

            // Tính khoảng thời gian overlap giữa rental period và filter range
            // effectiveStartDate = max(rentDate, filterStartDate) - ngày bắt đầu tính revenue
            LocalDate effectiveStartDate = filterStartDate != null ?
                (rentDate.isAfter(filterStartDate) ? rentDate : filterStartDate) : rentDate;

            // effectiveEndDate = min(returnDate, filterEndDate) - ngày kết thúc tính revenue
            LocalDate effectiveEndDate = filterEndDate != null ?
                (returnDate.isBefore(filterEndDate) ? returnDate : filterEndDate) : returnDate;

            // Nếu không có overlap, revenue = 0
            if (effectiveStartDate.isAfter(effectiveEndDate)) {
                return BigDecimal.ZERO;
            }

            // Tính số ngày overlap (bao gồm cả ngày đầu và cuối)
            long overlapDays = ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate) + 1;

            // Đảm bảo không có số ngày âm hoặc bằng 0
            if (overlapDays <= 0) {
                return BigDecimal.ZERO;
            }

            // Tính revenue: giá thuê một ngày × số ngày overlap × số lượng
            // costumeBill.getRentPrice() đã là giá thuê một ngày rồi
            BigDecimal proportionalRevenue = costumeBill.getRentPrice()
                                                       .multiply(BigDecimal.valueOf(overlapDays))
                                                       .multiply(BigDecimal.valueOf(costumeBill.getQuantity()));

            // Debug logging để theo dõi quá trình tính toán
            System.out.println("=== DEBUG: Tính toán Revenue theo tỷ lệ (Đã sửa) ===");
            System.out.println("Trang phục: " + costumeBill.getCostume().getName());
            System.out.println("Ngày thuê: " + rentDate + ", Ngày trả: " + returnDate);
            System.out.println("Khoảng filter: " + filterStartDate + " đến " + filterEndDate);
            System.out.println("Khoảng tính revenue: " + effectiveStartDate + " đến " + effectiveEndDate);
            System.out.println("Số ngày overlap: " + overlapDays);
            System.out.println("Giá thuê/ngày: " + costumeBill.getRentPrice() + ", Số lượng: " + costumeBill.getQuantity());
            System.out.println("Revenue theo tỷ lệ: " + proportionalRevenue);

            return proportionalRevenue;
        } catch (Exception e) {
            System.err.println("Lỗi khi tính revenue theo tỷ lệ: " + e.getMessage());
            e.printStackTrace();
            // Fallback: tính theo số ngày từ rentDate đến returnDate (hoặc hiện tại)
            try {
                LocalDate rentDate = parseDateObject(bill.get("rentDate"));
                LocalDate returnDate = parseDateObject(bill.get("returnDate"));
                if (returnDate == null) {
                    returnDate = LocalDate.now();
                }
                if (rentDate != null) {
                    long days = ChronoUnit.DAYS.between(rentDate, returnDate) + 1;
                    return costumeBill.getRentPrice().multiply(BigDecimal.valueOf(days)).multiply(BigDecimal.valueOf(costumeBill.getQuantity()));
                }
            } catch (Exception fallbackException) {
                System.err.println("Lỗi trong fallback calculation: " + fallbackException.getMessage());
            }
            // Fallback cuối cùng: giả sử thuê 1 ngày
            return costumeBill.getRentPrice().multiply(BigDecimal.valueOf(costumeBill.getQuantity()));
        }
    }
}