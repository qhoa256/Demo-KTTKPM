package com.costumeRental.billcostumeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BillCostumeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillCostumeServiceApplication.class, args);
    }
} 