package com.costumeRental.importbillservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportingBill {
    private Long id;
    private Long managerId;
    private Long supplierId;
    private LocalDate importDate;
} 