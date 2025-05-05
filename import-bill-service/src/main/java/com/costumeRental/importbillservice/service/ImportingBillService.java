package com.costumeRental.importbillservice.service;

import com.costumeRental.importbillservice.model.ImportingBill;

import java.util.List;

public interface ImportingBillService {
    List<ImportingBill> getAllImportingBills();
    ImportingBill getImportingBillById(Long id);
    ImportingBill createImportingBill(ImportingBill importingBill);
    ImportingBill updateImportingBill(Long id, ImportingBill importingBill);
    void deleteImportingBill(Long id);
} 