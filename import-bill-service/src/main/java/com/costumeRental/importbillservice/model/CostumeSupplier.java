package com.costumeRental.importbillservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostumeSupplier {
    private Long id;
    private Costume costume;
    private Supplier supplier;
} 