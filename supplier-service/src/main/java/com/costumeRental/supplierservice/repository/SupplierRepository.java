package com.costumeRental.supplierservice.repository;

import com.costumeRental.supplierservice.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // Add custom queries if needed
} 