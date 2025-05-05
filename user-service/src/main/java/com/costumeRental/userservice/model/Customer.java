package com.costumeRental.userservice.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tblCustomer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Customer")
@JsonTypeName("Customer")
public class Customer extends User {
    @Column(name = "UserId", insertable = false, updatable = false)
    private Long userId;
    
    private Integer rewardPoint;
    private String customerRanking;
} 