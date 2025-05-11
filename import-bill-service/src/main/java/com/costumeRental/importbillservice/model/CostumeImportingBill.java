package com.costumeRental.importbillservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostumeImportingBill {
    private Long id;
    private CostumeSupplier costumeSupplier;
    private BigDecimal importPrice;
    private int quantity;
    private String name;
    private String description;
} 