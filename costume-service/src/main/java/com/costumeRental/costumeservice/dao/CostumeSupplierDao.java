package com.costumeRental.costumeservice.dao;

import com.costumeRental.costumeservice.model.CostumeSupplier;
import java.util.List;

public interface CostumeSupplierDao {
    List<CostumeSupplier> findAll();
    CostumeSupplier findById(Long id);
    CostumeSupplier save(CostumeSupplier costumeSupplier);
    void deleteById(Long id);
    List<CostumeSupplier> findBySupplierId(String supplierId);
} 