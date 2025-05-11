package com.costumeRental.importbillservice.service;

import com.costumeRental.importbillservice.model.CostumeImportingBill;
import com.costumeRental.importbillservice.model.ImportingBill;

import java.util.List;

public interface ImportingBillService {
    List<ImportingBill> getAllImportingBills();
    ImportingBill getImportingBillById(Long id);
    ImportingBill createImportingBill(ImportingBill importingBill);
    ImportingBill updateImportingBill(Long id, ImportingBill importingBill);
    void deleteImportingBill(Long id);
    
    // Methods for CostumeImportingBill
    List<CostumeImportingBill> getCostumeImportingBillsByImportingBillId(Long importingBillId);
    CostumeImportingBill addCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill);
    CostumeImportingBill updateCostumeImportingBill(Long importingBillId, Long id, CostumeImportingBill costumeImportingBill);
    void deleteCostumeImportingBill(Long id);
} 