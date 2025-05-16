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
     * @param rentDate Ngày bắt đầu thuê (định dạng yyyy-MM-dd)
     * @param returnDate Ngày trả (định dạng yyyy-MM-dd)
     * @return Danh sách ID của các trang phục đang được thuê
     */
    @GetMapping("/rented-costumes")
    public ResponseEntity<List<Map<String, Object>>> getRentedCostumes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        
        // Lấy danh sách các hóa đơn có ngày thuê và ngày trả giao với khoảng thời gian đã cho
        List<Bill> overlappingBills = billService.getAllBills().stream()
            .filter(bill -> {
                // Kiểm tra xem hóa đơn có giao với khoảng thời gian đã cho không
                LocalDate billRentDate = bill.getRentDate();
                LocalDate billReturnDate = bill.getReturnDate();
                
                // Các trường hợp giao nhau:
                // 1. Ngày thuê của hóa đơn nằm trong khoảng thời gian
                // 2. Ngày trả của hóa đơn nằm trong khoảng thời gian
                // 3. Khoảng thời gian nằm hoàn toàn trong khoảng thời gian thuê của hóa đơn
                return (billRentDate.isBefore(returnDate) || billRentDate.isEqual(returnDate)) &&
                       (billReturnDate.isAfter(rentDate) || billReturnDate.isEqual(rentDate));
            })
            .collect(Collectors.toList());
        
        // Biến đổi thành danh sách các trang phục (giả định rằng mỗi hóa đơn có thể chứa nhiều trang phục)
        // Đối với API này, chúng ta trả về ID của hoá đơn và ID của trang phục
        // Trong triển khai thực tế, bạn cần thay đổi điều này để trả về danh sách chi tiết từ bảng bill_costume
        List<Map<String, Object>> rentedCostumes = overlappingBills.stream()
            .map(bill -> {
                Map<String, Object> rentedCostume = new HashMap<>();
                rentedCostume.put("billId", bill.getId());
                // Giả định rằng bạn có thể lấy danh sách các ID trang phục từ bảng bill_costume
                // Trong triển khai thực tế, bạn cần thay đổi điều này dựa trên cấu trúc dữ liệu thực tế của bạn
                // rentedCostume.put("costumeIds", getCostumeIdsForBill(bill.getId()));
                rentedCostume.put("costumeId", 1L); // Thay thế bằng mã lấy ID trang phục thực tế từ bill
                rentedCostume.put("rentDate", bill.getRentDate());
                rentedCostume.put("returnDate", bill.getReturnDate());
                return rentedCostume;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(rentedCostumes);
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