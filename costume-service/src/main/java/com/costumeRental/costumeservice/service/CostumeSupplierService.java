package com.costumeRental.costumeservice.service;

import com.costumeRental.costumeservice.model.CostumeSupplier;
import java.util.List;

public interface CostumeSupplierService {
    List<CostumeSupplier> findAll();
    CostumeSupplier findById(Long id);
    CostumeSupplier save(CostumeSupplier costumeSupplier);
    void deleteById(Long id);
    List<CostumeSupplier> findBySupplierId(Long supplierId);
} 