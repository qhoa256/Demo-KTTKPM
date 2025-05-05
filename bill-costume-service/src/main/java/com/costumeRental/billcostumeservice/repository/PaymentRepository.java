package com.costumeRental.billcostumeservice.repository;

import com.costumeRental.billcostumeservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Add custom queries if needed
} 