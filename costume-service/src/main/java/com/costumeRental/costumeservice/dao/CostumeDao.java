package com.costumeRental.costumeservice.dao;

import com.costumeRental.costumeservice.model.Costume;

import java.util.List;
import java.util.Optional;

public interface CostumeDao {
    List<Costume> findAll();
    Optional<Costume> findById(Long id);
    List<Costume> findByCategory(String category);
    Costume save(Costume costume);
    void deleteById(Long id);
} 