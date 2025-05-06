package com.costumeRental.importbillservice.service;

import com.costumeRental.importbillservice.dao.ImportingBillDao;
import com.costumeRental.importbillservice.model.ImportingBill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportingBillServiceImpl implements ImportingBillService {
    private final ImportingBillDao importingBillDao;

    @Override
    public List<ImportingBill> getAllImportingBills() {
        return importingBillDao.findAll();
    }

    @Override
    public ImportingBill getImportingBillById(Long id) {
        return importingBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Importing bill not found with id: " + id));
    }

    @Override
    public ImportingBill createImportingBill(ImportingBill importingBill) {
        return importingBillDao.save(importingBill);
    }

    @Override
    public ImportingBill updateImportingBill(Long id, ImportingBill importingBill) {
        ImportingBill existingImportingBill = getImportingBillById(id);
        
        existingImportingBill.setManagerId(importingBill.getManagerId());
        existingImportingBill.setSupplierId(importingBill.getSupplierId());
        existingImportingBill.setImportDate(importingBill.getImportDate());
        
        return importingBillDao.save(existingImportingBill);
    }

    @Override
    public void deleteImportingBill(Long id) {
        ImportingBill importingBill = getImportingBillById(id);
        importingBillDao.delete(importingBill);
    }
} 