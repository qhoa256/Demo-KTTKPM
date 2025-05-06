package com.costumeRental.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostumeImportingBill {
    private Long id;
    private Costume costume;
    private BigDecimal importPrice;
    private String note;
    private String importingBillId;
    private int quantity;
    private String name;
    private String color;
    private String size;
} 