package com.costumeRental.billcostumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullName {
    private Long id;
    private String firstName;
    private String lastName;
} 