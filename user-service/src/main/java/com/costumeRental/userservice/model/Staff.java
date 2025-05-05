package com.costumeRental.userservice.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tblStaff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Staff")
@JsonTypeName("Staff")
public class Staff extends User {
    @Column(name = "UserId", insertable = false, updatable = false)
    private Long userId;
    
    private BigDecimal salary;
    private String position;
    private String managerCode;
    private String title;
} 