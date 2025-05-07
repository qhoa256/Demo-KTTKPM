package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.model.CostumeBill;
import com.costumeRental.costumeservice.service.CostumeBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeBillServiceImpl implements CostumeBillService {

    private final CostumeBillDao costumeBillDao;

    @Override
    public CostumeBill createCostumeBill(CostumeBill costumeBill) {
        return costumeBillDao.save(costumeBill);
    }

    @Override
    public CostumeBill getCostumeBillById(Long id) {
        return costumeBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));
    }

    @Override
    public List<CostumeBill> getAllCostumeBills() {
        return costumeBillDao.findAll();
    }

    @Override
    public List<CostumeBill> getCostumeBillsByBillId(String billId) {
        return costumeBillDao.findByBillId(billId);
    }

    @Override
    public List<CostumeBill> getCostumeBillsByCostumeId(Long costumeId) {
        return costumeBillDao.findByCostumeId(costumeId);
    }

    @Override
    public CostumeBill updateCostumeBill(Long id, CostumeBill costumeBillDetails) {
        CostumeBill existingCostumeBill = costumeBillDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CostumeBill not found with id: " + id));

        existingCostumeBill.setCostume(costumeBillDetails.getCostume());
        existingCostumeBill.setRentPrice(costumeBillDetails.getRentPrice());
        existingCostumeBill.setBillId(costumeBillDetails.getBillId());
        existingCostumeBill.setQuantity(costumeBillDetails.getQuantity());
        existingCostumeBill.setName(costumeBillDetails.getName());
        existingCostumeBill.setDescription(costumeBillDetails.getDescription());
        
        return costumeBillDao.save(existingCostumeBill);
    }

    @Override
    public void deleteCostumeBill(Long id) {
        if (!costumeBillDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("CostumeBill not found with id: " + id);
        }
        costumeBillDao.deleteById(id);
    }
} 