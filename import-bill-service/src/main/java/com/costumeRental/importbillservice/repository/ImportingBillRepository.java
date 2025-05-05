package com.costumeRental.importbillservice.repository;

import com.costumeRental.importbillservice.model.ImportingBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportingBillRepository extends JpaRepository<ImportingBill, Long> {
    // Add custom queries if needed
} 