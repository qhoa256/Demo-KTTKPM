package com.costumeRental.billcostumeservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String username;
    private Address address;
    private FullName fullName;
    
    @JsonProperty("loyaltyPoints")
    private int loyaltyPoints;
} 