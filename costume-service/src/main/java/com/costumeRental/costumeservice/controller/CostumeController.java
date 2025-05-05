package com.costumeRental.costumeservice.controller;

import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.service.CostumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costumes")
@RequiredArgsConstructor
public class CostumeController {

    private final CostumeService costumeService;

    @PostMapping
    public ResponseEntity<Costume> createCostume(@RequestBody Costume costume) {
        return new ResponseEntity<>(costumeService.createCostume(costume), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Costume> getCostumeById(@PathVariable Long id) {
        return ResponseEntity.ok(costumeService.getCostumeById(id));
    }

    @GetMapping
    public ResponseEntity<List<Costume>> getAllCostumes() {
        return ResponseEntity.ok(costumeService.getAllCostumes());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Costume>> getCostumesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(costumeService.getCostumesByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Costume> updateCostume(@PathVariable Long id, @RequestBody Costume costume) {
        return ResponseEntity.ok(costumeService.updateCostume(id, costume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostume(@PathVariable Long id) {
        costumeService.deleteCostume(id);
        return ResponseEntity.noContent().build();
    }
} 