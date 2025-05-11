package com.costumeRental.importbillservice.service;

import com.costumeRental.importbillservice.dao.ImportingBillDao;
import com.costumeRental.importbillservice.model.CostumeImportingBill;
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
        
        existingImportingBill.setManager(importingBill.getManager());
        existingImportingBill.setSupplier(importingBill.getSupplier());
        existingImportingBill.setImportDate(importingBill.getImportDate());
        existingImportingBill.setCostumeImportingBills(importingBill.getCostumeImportingBills());
        
        return importingBillDao.save(existingImportingBill);
    }

    @Override
    public void deleteImportingBill(Long id) {
        ImportingBill importingBill = getImportingBillById(id);
        importingBillDao.delete(importingBill);
    }
    
    @Override
    public List<CostumeImportingBill> getCostumeImportingBillsByImportingBillId(Long importingBillId) {
        // Verify the importing bill exists
        getImportingBillById(importingBillId);
        return importingBillDao.findCostumeImportingBillsByImportingBillId(importingBillId);
    }
    
    @Override
    public CostumeImportingBill addCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill) {
        // Verify the importing bill exists
        getImportingBillById(importingBillId);
        return importingBillDao.saveCostumeImportingBill(importingBillId, costumeImportingBill);
    }
    
    @Override
    public CostumeImportingBill updateCostumeImportingBill(Long importingBillId, Long id, CostumeImportingBill costumeImportingBill) {
        // Verify the importing bill exists
        getImportingBillById(importingBillId);
        
        // Set the ID to ensure we're updating the existing record
        costumeImportingBill.setId(id);
        
        return importingBillDao.saveCostumeImportingBill(importingBillId, costumeImportingBill);
    }
    
    @Override
    public void deleteCostumeImportingBill(Long id) {
        importingBillDao.deleteCostumeImportingBill(id);
    }
} 