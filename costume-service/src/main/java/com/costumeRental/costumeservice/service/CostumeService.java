package com.costumeRental.costumeservice.service;

import com.costumeRental.costumeservice.model.Costume;

import java.util.List;

public interface CostumeService {
    Costume createCostume(Costume costume);
    Costume getCostumeById(Long id);
    List<Costume> getAllCostumes();
    List<Costume> getCostumesByCategory(String category);
    Costume updateCostume(Long id, Costume costume);
    void deleteCostume(Long id);
} 