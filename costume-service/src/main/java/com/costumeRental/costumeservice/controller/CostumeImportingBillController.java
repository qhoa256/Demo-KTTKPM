package com.costumeRental.costumeservice.controller;

import com.costumeRental.costumeservice.model.CostumeImportingBill;
import com.costumeRental.costumeservice.service.CostumeImportingBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costume-importing-bills")
@RequiredArgsConstructor
public class CostumeImportingBillController {

    private final CostumeImportingBillService costumeImportingBillService;

    @PostMapping
    public ResponseEntity<CostumeImportingBill> createCostumeImportingBill(@RequestBody CostumeImportingBill costumeImportingBill) {
        return new ResponseEntity<>(costumeImportingBillService.createCostumeImportingBill(costumeImportingBill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumeImportingBill> getCostumeImportingBillById(@PathVariable Long id) {
        return ResponseEntity.ok(costumeImportingBillService.getCostumeImportingBillById(id));
    }

    @GetMapping
    public ResponseEntity<List<CostumeImportingBill>> getAllCostumeImportingBills() {
        return ResponseEntity.ok(costumeImportingBillService.getAllCostumeImportingBills());
    }

    @GetMapping("/costume-supplier/{costumeSupplierIdId}")
    public ResponseEntity<List<CostumeImportingBill>> getCostumeImportingBillsByCostumeSupplier(@PathVariable Long costumeSupplierIdId) {
        return ResponseEntity.ok(costumeImportingBillService.getCostumeImportingBillsByCostumeSupplier(costumeSupplierIdId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostumeImportingBill> updateCostumeImportingBill(@PathVariable Long id, @RequestBody CostumeImportingBill costumeImportingBill) {
        return ResponseEntity.ok(costumeImportingBillService.updateCostumeImportingBill(id, costumeImportingBill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostumeImportingBill(@PathVariable Long id) {
        costumeImportingBillService.deleteCostumeImportingBill(id);
        return ResponseEntity.noContent().build();
    }
} 