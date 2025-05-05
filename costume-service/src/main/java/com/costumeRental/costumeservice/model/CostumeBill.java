package com.costumeRental.costumeservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tblCostumeBill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostumeBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "CostumeId")
    @JsonBackReference(value = "costume-bill")
    private Costume costume;
    
    private BigDecimal rentPrice;
    private String billId;
} 