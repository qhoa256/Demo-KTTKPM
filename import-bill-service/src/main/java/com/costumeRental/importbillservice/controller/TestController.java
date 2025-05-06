package com.costumeRental.importbillservice.controller;

import com.costumeRental.importbillservice.dao.ImportingBillDao;
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
    
    private final ImportingBillDao importingBillDao;
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        Long importingBillCount = (long) importingBillDao.findAll().size();
        return ResponseEntity.ok(Map.of("importingBillCount", importingBillCount));
    }
} 