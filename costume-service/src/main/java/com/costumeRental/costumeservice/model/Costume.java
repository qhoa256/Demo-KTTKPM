package com.costumeRental.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Costume {
    private Long id;
    private String category;
    private List<CostumeBill> costumeBills;
    private List<CostumeImportingBill> costumeImportingBills;
} 