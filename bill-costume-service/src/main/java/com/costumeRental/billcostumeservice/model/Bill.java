package com.costumeRental.billcostumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private Long id;
    private Customer customer;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private Payment payment;
    private String note;
    private String address;
} 