package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeImportingBillDao;
import com.costumeRental.costumeservice.dao.impl.CostumeImportingBillDaoImpl;
import com.costumeRental.costumeservice.model.CostumeImportingBill;
import com.costumeRental.costumeservice.service.CostumeImportingBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeImportingBillServiceImpl implements CostumeImportingBillService {

    private final CostumeImportingBillDao costumeImportingBillDao;

    @Override
    public CostumeImportingBill createCostumeImportingBill(CostumeImportingBill costumeImportingBill) {
        return costumeImportingBillDao.save(costumeImportingBill);
    }
    
    @Override
    public CostumeImportingBill createCostumeImportingBillWithImportingBill(CostumeImportingBill costumeImportingBill, Long importingBillId) {
        CostumeImportingBill savedBill = costumeImportingBillDao.save(costumeImportingBill);
        ((CostumeImportingBillDaoImpl)costumeImportingBillDao).linkToImportingBill(savedBill.getId(), importingBillId);
        return savedBill;
    }

    @Override
    public CostumeImportingBill getCostumeImportingBillById(Long id) {
        return costumeImportingBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeImportingBill not found with id: " + id));
    }

    @Override
    public List<CostumeImportingBill> getAllCostumeImportingBills() {
        return costumeImportingBillDao.findAll();
    }

    @Override
    public List<CostumeImportingBill> getCostumeImportingBillsByCostumeSupplier(Long costumeSupplierIdId) {
        return costumeImportingBillDao.findByCostumeSupplier(costumeSupplierIdId);
    }

    @Override
    public List<CostumeImportingBill> getCostumeImportingBillsByImportingBillId(Long importingBillId) {
        return costumeImportingBillDao.findByImportingBillId(importingBillId);
    }

    @Override
    public CostumeImportingBill updateCostumeImportingBill(Long id, CostumeImportingBill costumeImportingBillDetails) {
        CostumeImportingBill existingCostumeImportingBill = costumeImportingBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeImportingBill not found with id: " + id));

        existingCostumeImportingBill.setCostumeSupplier(costumeImportingBillDetails.getCostumeSupplier());
        existingCostumeImportingBill.setImportPrice(costumeImportingBillDetails.getImportPrice());
        existingCostumeImportingBill.setQuantity(costumeImportingBillDetails.getQuantity());
        existingCostumeImportingBill.setName(costumeImportingBillDetails.getName());
        existingCostumeImportingBill.setDescription(costumeImportingBillDetails.getDescription());
        
        return costumeImportingBillDao.save(existingCostumeImportingBill);
    }

    @Override
    public void deleteCostumeImportingBill(Long id) {
        if (!costumeImportingBillDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("CostumeImportingBill not found with id: " + id);
        }
        costumeImportingBillDao.deleteById(id);
    }
} 