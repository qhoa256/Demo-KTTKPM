package com.costumeRental.costumeservice.dao;

import com.costumeRental.costumeservice.model.CostumeBill;

import java.util.List;
import java.util.Optional;

public interface CostumeBillDao {
    List<CostumeBill> findAll();
    Optional<CostumeBill> findById(Long id);
    List<CostumeBill> findByBillId(String billId);
    List<CostumeBill> findByCostumeId(Long costumeId);
    CostumeBill save(CostumeBill costumeBill);
    void deleteById(Long id);
} 