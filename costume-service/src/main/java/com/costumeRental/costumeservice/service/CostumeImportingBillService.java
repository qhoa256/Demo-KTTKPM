package com.costumeRental.costumeservice.service;

import com.costumeRental.costumeservice.model.CostumeImportingBill;

import java.util.List;

public interface CostumeImportingBillService {
    CostumeImportingBill createCostumeImportingBill(CostumeImportingBill costumeImportingBill);
    CostumeImportingBill getCostumeImportingBillById(Long id);
    List<CostumeImportingBill> getAllCostumeImportingBills();
    List<CostumeImportingBill> getCostumeImportingBillsByCostumeId(Long costumeId);
    CostumeImportingBill updateCostumeImportingBill(Long id, CostumeImportingBill costumeImportingBill);
    void deleteCostumeImportingBill(Long id);
} 