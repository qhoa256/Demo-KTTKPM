package com.costumeRental.costumeservice.controller;

import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.dao.CostumeImportingBillDao;
import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.model.CostumeImportingBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestDAOController {

    private final CostumeDao costumeDao;
    private final CostumeBillDao costumeBillDao;
    private final CostumeImportingBillDao costumeImportingBillDao;

    @Autowired
    public TestDAOController(@Lazy CostumeDao costumeDao, 
                             @Lazy CostumeBillDao costumeBillDao, 
                             @Lazy CostumeImportingBillDao costumeImportingBillDao) {
        this.costumeDao = costumeDao;
        this.costumeBillDao = costumeBillDao;
        this.costumeImportingBillDao = costumeImportingBillDao;
    }

    @PostMapping("/setup")
    public ResponseEntity<Map<String, Object>> setupTestData() {
        Map<String, Object> response = new HashMap<>();
        
        // Create a new costume
        Costume costume = new Costume();
        costume.setCategory("Test Category");
        Costume savedCostume = costumeDao.save(costume);
        response.put("costume", savedCostume);
        
        // Create a costume bill
        CostumeBill costumeBill = new CostumeBill();
        costumeBill.setCostume(savedCostume);
        costumeBill.setRentPrice(new BigDecimal("100.00"));
        costumeBill.setBillId("TEST-BILL-001");
        costumeBill.setQuantity(1);
        costumeBill.setName("Test Costume");
        costumeBill.setColor("Red");
        costumeBill.setSize("M");
        CostumeBill savedCostumeBill = costumeBillDao.save(costumeBill);
        response.put("costumeBill", savedCostumeBill);
        
        // Create a costume importing bill
        CostumeImportingBill costumeImportingBill = new CostumeImportingBill();
        costumeImportingBill.setCostume(savedCostume);
        costumeImportingBill.setImportPrice(new BigDecimal("50.00"));
        costumeImportingBill.setNote("Test Note");
        costumeImportingBill.setImportingBillId("TEST-IMPORT-001");
        costumeImportingBill.setQuantity(10);
        costumeImportingBill.setName("Test Import Costume");
        costumeImportingBill.setColor("Blue");
        costumeImportingBill.setSize("L");
        CostumeImportingBill savedCostumeImportingBill = costumeImportingBillDao.save(costumeImportingBill);
        response.put("costumeImportingBill", savedCostumeImportingBill);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/costumes")
    public ResponseEntity<List<Costume>> getAllCostumes() {
        return ResponseEntity.ok(costumeDao.findAll());
    }
    
    @GetMapping("/costume-bills")
    public ResponseEntity<List<CostumeBill>> getAllCostumeBills() {
        return ResponseEntity.ok(costumeBillDao.findAll());
    }
    
    @GetMapping("/costume-importing-bills")
    public ResponseEntity<List<CostumeImportingBill>> getAllCostumeImportingBills() {
        return ResponseEntity.ok(costumeImportingBillDao.findAll());
    }
    
    @GetMapping("/costume/{id}")
    public ResponseEntity<Costume> getCostumeById(@PathVariable Long id) {
        return costumeDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/costume-bills/by-bill/{billId}")
    public ResponseEntity<List<CostumeBill>> getCostumeBillsByBillId(@PathVariable String billId) {
        return ResponseEntity.ok(costumeBillDao.findByBillId(billId));
    }
    
    @GetMapping("/costume-importing-bills/by-bill/{importingBillId}")
    public ResponseEntity<List<CostumeImportingBill>> getCostumeImportingBillsByImportingBillId(
            @PathVariable String importingBillId) {
        return ResponseEntity.ok(costumeImportingBillDao.findByImportingBillId(importingBillId));
    }
} 