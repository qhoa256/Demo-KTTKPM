package com.costumeRental.billcostumeservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblBill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long customerId;
    private LocalDateTime bookingTime;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PaymentId")
    @JsonManagedReference
    private Payment payment;
} 