package com.costumeRental.supplierservice.controller;

import com.costumeRental.supplierservice.dao.SupplierDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    private final SupplierDao supplierDao;
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        Long supplierCount = (long) supplierDao.findAll().size();
        return ResponseEntity.ok(Map.of("supplierCount", supplierCount));
    }
} 