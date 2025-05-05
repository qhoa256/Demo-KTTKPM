package com.costumeRental.importbillservice.controller;

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
} 