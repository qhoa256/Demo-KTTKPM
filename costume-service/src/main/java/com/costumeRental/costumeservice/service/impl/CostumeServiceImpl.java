package com.costumeRental.costumeservice.service.impl;

import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.service.CostumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostumeServiceImpl implements CostumeService {

    private final CostumeDao costumeDao;

    @Override
    public Costume createCostume(Costume costume) {
        return costumeDao.save(costume);
    }

    @Override
    public Costume getCostumeById(Long id) {
        return costumeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Costume not found with id: " + id));
    }

    @Override
    public List<Costume> getAllCostumes() {
        return costumeDao.findAll();
    }

    @Override
    public List<Costume> getCostumesByCategory(String category) {
        return costumeDao.findByCategory(category);
    }

    @Override
    public Costume updateCostume(Long id, Costume costumeDetails) {
        Costume existingCostume = costumeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Costume not found with id: " + id));

        existingCostume.setCategory(costumeDetails.getCategory());
        existingCostume.setName(costumeDetails.getName());
        existingCostume.setDescription(costumeDetails.getDescription());
        existingCostume.setPrice(costumeDetails.getPrice());
        
        return costumeDao.save(existingCostume);
    }

    @Override
    public void deleteCostume(Long id) {
        if (!costumeDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("Costume not found with id: " + id);
        }
        costumeDao.deleteById(id);
    }
} 