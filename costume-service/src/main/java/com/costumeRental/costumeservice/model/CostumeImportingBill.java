package com.costumeRental.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tblCostumeImportingBill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostumeImportingBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "CostumeId")
    private Costume costume;
    
    private BigDecimal importPrice;
    private String note;
    private String importingBillId;
} 