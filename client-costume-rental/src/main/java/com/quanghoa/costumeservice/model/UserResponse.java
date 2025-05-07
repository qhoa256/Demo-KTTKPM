package com.quanghoa.costumeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String type;
    private Long id;
    private String username;
    private String password;
    private Address address;
    private FullName fullName;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private Long id;
        private String street;
        private String city;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullName {
        private Long id;
        private String firstName;
        private String lastName;
    }
} 