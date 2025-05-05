package com.costumeRental.billcostumeservice.repository;

import com.costumeRental.billcostumeservice.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
    // Add custom queries if needed
} 