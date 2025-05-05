package com.costumeRental.importbillservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tblImportingBill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportingBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long managerId;
    private Long supplierId;
    private LocalDate importDate;
} 