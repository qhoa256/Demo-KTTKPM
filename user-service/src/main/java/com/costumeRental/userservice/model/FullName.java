package com.costumeRental.userservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tblFullName")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    
    @OneToOne(mappedBy = "fullName")
    @JsonBackReference
    private User user;
} 