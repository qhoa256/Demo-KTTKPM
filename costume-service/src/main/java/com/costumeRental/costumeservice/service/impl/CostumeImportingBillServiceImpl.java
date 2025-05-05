package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.model.CostumeImportingBill;
import com.costumeRental.costumeservice.repository.CostumeImportingBillRepository;
import com.costumeRental.costumeservice.service.CostumeImportingBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeImportingBillServiceImpl implements CostumeImportingBillService {

    private final CostumeImportingBillRepository costumeImportingBillRepository;

    @Override
    public CostumeImportingBill createCostumeImportingBill(CostumeImportingBill costumeImportingBill) {
        return costumeImportingBillRepository.save(costumeImportingBill);
    }

    @Override
    public CostumeImportingBill getCostumeImportingBillById(Long id) {
        return costumeImportingBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeImportingBill not found with id: " + id));
    }

    @Override
    public List<CostumeImportingBill> getAllCostumeImportingBills() {
        return costumeImportingBillRepository.findAll();
    }

    @Override
    public List<CostumeImportingBill> getCostumeImportingBillsByImportingBillId(String importingBillId) {
        return costumeImportingBillRepository.findByImportingBillId(importingBillId);
    }

    @Override
    public List<CostumeImportingBill> getCostumeImportingBillsByCostumeId(Long costumeId) {
        return costumeImportingBillRepository.findByCostumeId(costumeId);
    }

    @Override
    public CostumeImportingBill updateCostumeImportingBill(Long id, CostumeImportingBill costumeImportingBillDetails) {
        CostumeImportingBill existingCostumeImportingBill = costumeImportingBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeImportingBill not found with id: " + id));

        existingCostumeImportingBill.setCostume(costumeImportingBillDetails.getCostume());
        existingCostumeImportingBill.setImportPrice(costumeImportingBillDetails.getImportPrice());
        existingCostumeImportingBill.setNote(costumeImportingBillDetails.getNote());
        existingCostumeImportingBill.setImportingBillId(costumeImportingBillDetails.getImportingBillId());
        
        return costumeImportingBillRepository.save(existingCostumeImportingBill);
    }

    @Override
    public void deleteCostumeImportingBill(Long id) {
        if (!costumeImportingBillRepository.existsById(id)) {
            throw new EntityNotFoundException("CostumeImportingBill not found with id: " + id);
        }
        costumeImportingBillRepository.deleteById(id);
    }
} 