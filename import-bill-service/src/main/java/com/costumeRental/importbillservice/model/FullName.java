package com.costumeRental.importbillservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    
    @JsonBackReference
    private User user;
} 