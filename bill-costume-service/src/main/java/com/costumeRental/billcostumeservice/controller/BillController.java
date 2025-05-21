package com.costumeRental.billcostumeservice.controller;

import com.costumeRental.billcostumeservice.model.Bill;
import com.costumeRental.billcostumeservice.service.BillService;
import com.costumeRental.billcostumeservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;
    private final StatisticsService statisticsService;
    
    @Autowired
    public BillController(BillService billService, 
                         @Qualifier("cachingStatisticsDecorator") StatisticsService statisticsService) {
        this.billService = billService;
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }
    
    /**
     * Endpoint để lấy danh sách các trang phục đang được thuê trong khoảng thời gian
     * 
     * @param rentDate Ngày khách hàng muốn thuê (định dạng yyyy-MM-dd)
     * @param returnDate Ngày khách hàng sẽ trả (định dạng yyyy-MM-dd)
     * @return Danh sách ID của các trang phục không khả dụng trong khoảng thời gian này
     */
    @GetMapping("/rented-costumes")
    public ResponseEntity<List<Map<String, Object>>> getRentedCostumes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        
        // Xác thực đầu vào
        if (rentDate.isAfter(returnDate)) {
            throw new IllegalArgumentException("Ngày thuê phải trước ngày trả.");
        }
        
        // Lấy danh sách các hóa đơn có ngày thuê và ngày trả giao với khoảng thời gian đã cho
        List<Bill> overlappingBills = billService.getAllBills().stream()
            .filter(bill -> {
                // Kiểm tra nếu bill có null data
                if (bill.getRentDate() == null || bill.getReturnDate() == null) {
                    return false;
                }
                
                LocalDate billRentDate = bill.getRentDate();
                LocalDate billReturnDate = bill.getReturnDate();
                
                // Nếu trang phục đã được trả trước khi người dùng muốn thuê (billReturnDate < rentDate) => Khả dụng
                if (billReturnDate.isBefore(rentDate)) {
                    return false;
                }
                
                // Nếu trang phục chỉ được thuê sau khi người dùng đã trả (billRentDate > returnDate) => Khả dụng
                if (billRentDate.isAfter(returnDate)) {
                    return false;
                }
                
                // Trong mọi trường hợp khác, trang phục không khả dụng trong khoảng thời gian
                return true;
            })
            .collect(Collectors.toList());
        

        List<Map<String, Object>> rentedCostumes = new java.util.ArrayList<>();
        
        for (Bill bill : overlappingBills) {
            List<Long> costumeIds = getCostumeIdsFromBill(bill); 
            
            for (Long costumeId : costumeIds) {
                Map<String, Object> rentedItem = new HashMap<>();
                rentedItem.put("billId", bill.getId());
                rentedItem.put("costumeId", costumeId);
                rentedItem.put("rentDate", bill.getRentDate());
                rentedItem.put("returnDate", bill.getReturnDate());
                rentedCostumes.add(rentedItem);
            }
        }
        
        return ResponseEntity.ok(rentedCostumes);
    }
    

    private List<Long> getCostumeIdsFromBill(Bill bill) {
        return List.of(1L, 2L, 3L);
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.createBill(bill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody Bill bill) {
        return ResponseEntity.ok(billService.updateBill(id, bill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics/revenue-by-category")
    public ResponseEntity<Map<String, Object>> getRevenueByCategory() {
        return ResponseEntity.ok(statisticsService.getRevenueByCategory());
    }
} 