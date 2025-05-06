package com.costumeRental.costumeservice.dao;

import com.costumeRental.costumeservice.model.CostumeImportingBill;

import java.util.List;
import java.util.Optional;

public interface CostumeImportingBillDao {
    List<CostumeImportingBill> findAll();
    Optional<CostumeImportingBill> findById(Long id);
    List<CostumeImportingBill> findByImportingBillId(String importingBillId);
    List<CostumeImportingBill> findByCostumeId(Long costumeId);
    CostumeImportingBill save(CostumeImportingBill costumeImportingBill);
    void deleteById(Long id);
} 