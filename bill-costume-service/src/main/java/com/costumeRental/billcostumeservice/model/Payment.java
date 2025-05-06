package com.costumeRental.billcostumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private String paymentMethod;
    private String paymentNote;
} 