package com.costumeRental.costumeservice.repository;

import com.costumeRental.costumeservice.model.CostumeBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostumeBillRepository extends JpaRepository<CostumeBill, Long> {
    List<CostumeBill> findByBillId(String billId);
    List<CostumeBill> findByCostumeId(Long costumeId);
} 