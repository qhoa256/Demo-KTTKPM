package com.costumeRental.billcostumeservice.dao;

import com.costumeRental.billcostumeservice.model.Bill;

import java.util.List;
import java.util.Optional;

public interface BillDao {
    List<Bill> findAll();
    Optional<Bill> findById(Long id);
    Bill save(Bill bill);
    void deleteById(Long id);
} 