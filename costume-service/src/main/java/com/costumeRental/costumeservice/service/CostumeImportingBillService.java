package com.costumeRental.costumeservice.service;

import com.costumeRental.costumeservice.model.CostumeImportingBill;

import java.util.List;

public interface CostumeImportingBillService {
    CostumeImportingBill createCostumeImportingBill(CostumeImportingBill costumeImportingBill);
    CostumeImportingBill createCostumeImportingBillWithImportingBill(CostumeImportingBill costumeImportingBill, Long importingBillId);
    CostumeImportingBill getCostumeImportingBillById(Long id);
    List<CostumeImportingBill> getAllCostumeImportingBills();
    List<CostumeImportingBill> getCostumeImportingBillsByCostumeSupplier(Long costumeSupplierIdId);
    List<CostumeImportingBill> getCostumeImportingBillsByImportingBillId(Long importingBillId);
    CostumeImportingBill updateCostumeImportingBill(Long id, CostumeImportingBill costumeImportingBill);
    void deleteCostumeImportingBill(Long id);
} 