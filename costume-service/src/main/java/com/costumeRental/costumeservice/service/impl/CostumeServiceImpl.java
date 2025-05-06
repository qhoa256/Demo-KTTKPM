package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.repository.CostumeRepository;
import com.costumeRental.costumeservice.service.CostumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeServiceImpl implements CostumeService {

    private final CostumeRepository costumeRepository;

    @Override
    public Costume createCostume(Costume costume) {
        return costumeRepository.save(costume);
    }

    @Override
    public Costume getCostumeById(Long id) {
        return costumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Costume not found with id: " + id));
    }

    @Override
    public List<Costume> getAllCostumes() {
        return costumeRepository.findAll();
    }

    @Override
    public List<Costume> getCostumesByCategory(String category) {
        return costumeRepository.findByCategory(category);
    }

    @Override
    public Costume updateCostume(Long id, Costume costumeDetails) {
        Costume existingCostume = costumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Costume not found with id: " + id));

        existingCostume.setCategory(costumeDetails.getCategory());
        
        return costumeRepository.save(existingCostume);
    }

    @Override
    public void deleteCostume(Long id) {
        if (!costumeRepository.existsById(id)) {
            throw new EntityNotFoundException("Costume not found with id: " + id);
        }
        costumeRepository.deleteById(id);
    }
} 