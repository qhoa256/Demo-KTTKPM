package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.repository.CostumeBillRepository;
import com.costumeRental.costumeservice.service.CostumeBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeBillServiceImpl implements CostumeBillService {

    private final CostumeBillRepository costumeBillRepository;

    @Override
    public CostumeBill createCostumeBill(CostumeBill costumeBill) {
        return costumeBillRepository.save(costumeBill);
    }

    @Override
    public CostumeBill getCostumeBillById(Long id) {
        return costumeBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));
    }

    @Override
    public List<CostumeBill> getAllCostumeBills() {
        return costumeBillRepository.findAll();
    }

    @Override
    public List<CostumeBill> getCostumeBillsByBillId(String billId) {
        return costumeBillRepository.findByBillId(billId);
    }

    @Override
    public List<CostumeBill> getCostumeBillsByCostumeId(Long costumeId) {
        return costumeBillRepository.findByCostumeId(costumeId);
    }

    @Override
    public CostumeBill updateCostumeBill(Long id, CostumeBill costumeBillDetails) {
        CostumeBill existingCostumeBill = costumeBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));

        existingCostumeBill.setCostume(costumeBillDetails.getCostume());
        existingCostumeBill.setRentPrice(costumeBillDetails.getRentPrice());
        existingCostumeBill.setBillId(costumeBillDetails.getBillId());
        
        return costumeBillRepository.save(existingCostumeBill);
    }

    @Override
    public void deleteCostumeBill(Long id) {
        if (!costumeBillRepository.existsById(id)) {
            throw new EntityNotFoundException("CostumeBill not found with id: " + id);
        }
        costumeBillRepository.deleteById(id);
    }
} 