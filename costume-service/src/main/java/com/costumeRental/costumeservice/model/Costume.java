package com.costumeRental.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tblCostume")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Costume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String category;
    private BigDecimal price;
    
    @OneToMany(mappedBy = "costume", cascade = CascadeType.ALL)
    private List<CostumeBill> costumeBills;
    
    @OneToMany(mappedBy = "costume", cascade = CascadeType.ALL)
    private List<CostumeImportingBill> costumeImportingBills;
} 