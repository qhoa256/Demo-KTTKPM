package com.costumeRental.importbillservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportingBill {
    private Long id;
    private Staff manager;
    private Supplier supplier;
    private LocalDate importDate;
    private List<CostumeImportingBill> costumeImportingBills;
} 