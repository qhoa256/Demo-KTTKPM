package com.costumeRental.importbillservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class User {
    private Long id;
    private String username;
    private String password;
    
    @JsonManagedReference
    private Address address;
    
    @JsonManagedReference
    private FullName fullName;
} 