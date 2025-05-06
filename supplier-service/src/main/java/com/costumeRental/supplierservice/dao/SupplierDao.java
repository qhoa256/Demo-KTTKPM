package com.costumeRental.supplierservice.dao;

import com.costumeRental.supplierservice.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierDao {
    List<Supplier> findAll();
    Optional<Supplier> findById(Long id);
    Supplier save(Supplier supplier);
    void delete(Supplier supplier);
} 