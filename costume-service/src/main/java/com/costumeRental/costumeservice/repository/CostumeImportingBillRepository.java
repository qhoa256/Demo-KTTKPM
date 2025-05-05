package com.costumeRental.costumeservice.repository;

import com.costumeRental.costumeservice.model.CostumeImportingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostumeImportingBillRepository extends JpaRepository<CostumeImportingBill, Long> {
    List<CostumeImportingBill> findByImportingBillId(String importingBillId);
    List<CostumeImportingBill> findByCostumeId(Long costumeId);
} 