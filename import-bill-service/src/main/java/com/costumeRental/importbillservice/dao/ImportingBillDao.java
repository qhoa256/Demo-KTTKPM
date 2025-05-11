package com.costumeRental.importbillservice.dao;

import com.costumeRental.importbillservice.model.CostumeImportingBill;
import com.costumeRental.importbillservice.model.ImportingBill;

import java.util.List;
import java.util.Optional;

public interface ImportingBillDao {
    List<ImportingBill> findAll();
    Optional<ImportingBill> findById(Long id);
    ImportingBill save(ImportingBill importingBill);
    void delete(ImportingBill importingBill);
    
    // Methods for CostumeImportingBill
    List<CostumeImportingBill> findCostumeImportingBillsByImportingBillId(Long importingBillId);
    CostumeImportingBill saveCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill);
    void deleteCostumeImportingBill(Long id);
} 