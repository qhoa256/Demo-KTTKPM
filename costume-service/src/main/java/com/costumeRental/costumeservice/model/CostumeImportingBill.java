package com.costumeRental.costumeservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class CostumeImportingBill extends Costume {
    @ManyToOne
    @JoinColumn(name = "CostumeId")
    @JsonBackReference(value = "costume-importing-bill")
    private Costume costume;
    
    private BigDecimal importPrice;
    private String note;
    private String importingBillId;
    
    private int quantity;
    private String name;
    private String color;
    private String size;
} 