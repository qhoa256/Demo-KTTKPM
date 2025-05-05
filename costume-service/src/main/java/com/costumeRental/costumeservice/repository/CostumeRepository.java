package com.costumeRental.costumeservice.repository;

import com.costumeRental.costumeservice.model.Costume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostumeRepository extends JpaRepository<Costume, Long> {
    List<Costume> findByCategory(String category);
} 