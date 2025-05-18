package com.costumeRental.costumeservice.controller;

import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.CostumeBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/costume-bills")
@RequiredArgsConstructor
public class CostumeBillController {

    private final CostumeBillService costumeBillService;

    @PostMapping
    public ResponseEntity<CostumeBill> createCostumeBill(@RequestBody CostumeBill costumeBill) {
        return new ResponseEntity<>(costumeBillService.createCostumeBill(costumeBill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumeBill> getCostumeBillById(@PathVariable Long id) {
        return ResponseEntity.ok(costumeBillService.getCostumeBillById(id));
    }

    @GetMapping
    public ResponseEntity<List<CostumeBill>> getAllCostumeBills() {
        return ResponseEntity.ok(costumeBillService.getAllCostumeBills());
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<CostumeBill>> getCostumeBillsByBillId(@PathVariable String billId) {
        return ResponseEntity.ok(costumeBillService.getCostumeBillsByBillId(billId));
    }

    @GetMapping("/costume/{costumeId}")
    public ResponseEntity<List<CostumeBill>> getCostumeBillsByCostumeId(@PathVariable Long costumeId) {
        return ResponseEntity.ok(costumeBillService.getCostumeBillsByCostumeId(costumeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostumeBill> updateCostumeBill(@PathVariable Long id, @RequestBody CostumeBill costumeBill) {
        return ResponseEntity.ok(costumeBillService.updateCostumeBill(id, costumeBill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostumeBill(@PathVariable Long id) {
        costumeBillService.deleteCostumeBill(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/statistics/revenue-by-category")
    public ResponseEntity<Map<String, Object>> getRevenueByCategory() {
        return ResponseEntity.ok(costumeBillService.getRevenueByCategory());
    }
} 