package com.costumeRental.costumeservice.controller;

import com.costumeRental.costumeservice.model.CostumeSupplier;
import com.costumeRental.costumeservice.service.CostumeSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costume-suppliers")
@RequiredArgsConstructor
public class CostumeSupplierController {

    private final CostumeSupplierService costumeSupplierService;

    @PostMapping
    public ResponseEntity<CostumeSupplier> createCostumeSupplier(@RequestBody CostumeSupplier costumeSupplier) {
        return new ResponseEntity<>(costumeSupplierService.save(costumeSupplier), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumeSupplier> getCostumeSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(costumeSupplierService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CostumeSupplier>> getAllCostumeSuppliers() {
        return ResponseEntity.ok(costumeSupplierService.findAll());
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<CostumeSupplier>> getCostumesBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(costumeSupplierService.findBySupplierId(supplierId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostumeSupplier> updateCostumeSupplier(@PathVariable Long id, @RequestBody CostumeSupplier costumeSupplier) {
        CostumeSupplier existingSupplier = costumeSupplierService.findById(id);
        costumeSupplier.setId(id);
        return ResponseEntity.ok(costumeSupplierService.save(costumeSupplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostumeSupplier(@PathVariable Long id) {
        costumeSupplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 