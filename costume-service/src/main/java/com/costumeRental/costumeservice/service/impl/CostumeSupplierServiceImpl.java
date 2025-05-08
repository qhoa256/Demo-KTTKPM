package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeSupplierDao;
import com.costumeRental.costumeservice.model.CostumeSupplier;
import com.costumeRental.costumeservice.service.CostumeSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeSupplierServiceImpl implements CostumeSupplierService {

    private final CostumeSupplierDao costumeSupplierDao;

    @Override
    public List<CostumeSupplier> findAll() {
        return costumeSupplierDao.findAll();
    }

    @Override
    public CostumeSupplier findById(Long id) {
        CostumeSupplier supplier = costumeSupplierDao.findById(id);
        if (supplier == null) {
            throw new EntityNotFoundException("Costume supplier not found with id: " + id);
        }
        return supplier;
    }

    @Override
    public CostumeSupplier save(CostumeSupplier costumeSupplier) {
        return costumeSupplierDao.save(costumeSupplier);
    }

    @Override
    public void deleteById(Long id) {
        if (costumeSupplierDao.findById(id) == null) {
            throw new EntityNotFoundException("Costume supplier not found with id: " + id);
        }
        costumeSupplierDao.deleteById(id);
    }

    @Override
    public List<CostumeSupplier> findBySupplierId(String supplierId) {
        return costumeSupplierDao.findBySupplierId(supplierId);
    }
} 