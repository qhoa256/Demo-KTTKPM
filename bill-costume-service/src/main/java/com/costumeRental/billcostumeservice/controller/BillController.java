package com.costumeRental.billcostumeservice.controller;

import com.costumeRental.billcostumeservice.model.Bill;
import com.costumeRental.billcostumeservice.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
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
                
                // Một trang phục đang được thuê nếu:
                // 1. Nếu ngày trả của hóa đơn >= ngày thuê mới (người dùng muốn thuê từ rentDate)
                // VÀ
                // 2. Ngày thuê của hóa đơn <= ngày trả mới (người dùng muốn trả vào returnDate)
                //
                // Nói cách khác, costume chỉ KHÔNG khả dụng khi khoảng thời gian thuê 
                // chồng chéo với khoảng thời gian đã cho
                
                // Nếu trang phục đã được trả trước khi người dùng muốn thuê (billReturnDate < rentDate),
                // thì trang phục đó vẫn khả dụng
                if (billReturnDate.isBefore(rentDate)) {
                    return false;
                }
                
                // Nếu trang phục chỉ được thuê sau khi người dùng đã trả (billRentDate > returnDate),
                // thì trang phục đó vẫn khả dụng
                if (billRentDate.isAfter(returnDate)) {
                    return false;
                }
                
                // Trong mọi trường hợp khác, trang phục không khả dụng trong khoảng thời gian
                return true;
            })
            .collect(Collectors.toList());
        
        // Trong triển khai thực tế, bạn sẽ cần truy vấn bảng bill_items hoặc bill_costumes để lấy 
        // danh sách chi tiết các trang phục từ mỗi đơn hàng
        List<Map<String, Object>> rentedCostumes = new java.util.ArrayList<>();
        
        for (Bill bill : overlappingBills) {
            // Giả định rằng mỗi bill có thể chứa nhiều trang phục
            // Đây chỉ là ví dụ, trong triển khai thực tế bạn cần truy vấn bảng liên kết giữa Bill và Costume
            
            // Giả sử chúng ta có danh sách costume IDs từ một hàm giả định
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
    
    /**
     * Hàm giả định để lấy danh sách costume IDs từ một bill
     * Trong triển khai thực tế, đây sẽ là một truy vấn đến bảng bill_items hoặc bill_costumes
     */
    private List<Long> getCostumeIdsFromBill(Bill bill) {
        // Giả sử mỗi hóa đơn có một số trang phục mặc định
        // Đây chỉ là dữ liệu mẫu
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
} 