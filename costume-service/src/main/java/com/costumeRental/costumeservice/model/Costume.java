package com.costumeRental.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Costume {
    private Long id;
    private String category;
    private String name;
    private String description;
    private BigDecimal price;
} 