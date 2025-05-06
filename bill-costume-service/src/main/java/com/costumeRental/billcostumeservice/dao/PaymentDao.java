package com.costumeRental.billcostumeservice.dao;

import com.costumeRental.billcostumeservice.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentDao {
    List<Payment> findAll();
    Optional<Payment> findById(Long id);
    Payment save(Payment payment);
    void deleteById(Long id);
} 