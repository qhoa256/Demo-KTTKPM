package com.costumeRental.importbillservice.controller;

import com.costumeRental.importbillservice.model.CostumeImportingBill;
import com.costumeRental.importbillservice.model.ImportingBill;
import com.costumeRental.importbillservice.service.ImportingBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/importing-bills")
@RequiredArgsConstructor
public class ImportingBillController {
    private final ImportingBillService importingBillService;

    @GetMapping
    public ResponseEntity<List<ImportingBill>> getAllImportingBills() {
        return ResponseEntity.ok(importingBillService.getAllImportingBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportingBill> getImportingBillById(@PathVariable Long id) {
        return ResponseEntity.ok(importingBillService.getImportingBillById(id));
    }

    @PostMapping
    public ResponseEntity<ImportingBill> createImportingBill(@RequestBody ImportingBill importingBill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(importingBillService.createImportingBill(importingBill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportingBill> updateImportingBill(@PathVariable Long id, @RequestBody ImportingBill importingBill) {
        return ResponseEntity.ok(importingBillService.updateImportingBill(id, importingBill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportingBill(@PathVariable Long id) {
        importingBillService.deleteImportingBill(id);
        return ResponseEntity.noContent().build();
    }
    
    // CostumeImportingBill endpoints
    
    @GetMapping("/{importingBillId}/costume-importing-bills")
    public ResponseEntity<List<CostumeImportingBill>> getCostumeImportingBillsByImportingBillId(@PathVariable Long importingBillId) {
        return ResponseEntity.ok(importingBillService.getCostumeImportingBillsByImportingBillId(importingBillId));
    }
    
    @PostMapping("/{importingBillId}/costume-importing-bills")
    public ResponseEntity<CostumeImportingBill> addCostumeImportingBill(
            @PathVariable Long importingBillId,
            @RequestBody CostumeImportingBill costumeImportingBill) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(importingBillService.addCostumeImportingBill(importingBillId, costumeImportingBill));
    }
    
    @PutMapping("/{importingBillId}/costume-importing-bills/{id}")
    public ResponseEntity<CostumeImportingBill> updateCostumeImportingBill(
            @PathVariable Long importingBillId,
            @PathVariable Long id,
            @RequestBody CostumeImportingBill costumeImportingBill) {
        return ResponseEntity.ok(importingBillService.updateCostumeImportingBill(importingBillId, id, costumeImportingBill));
    }
    
    @DeleteMapping("/costume-importing-bills/{id}")
    public ResponseEntity<Void> deleteCostumeImportingBill(@PathVariable Long id) {
        importingBillService.deleteCostumeImportingBill(id);
        return ResponseEntity.noContent().build();
    }
} 