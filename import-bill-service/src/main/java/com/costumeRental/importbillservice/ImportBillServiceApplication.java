package com.costumeRental.importbillservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ImportBillServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImportBillServiceApplication.class, args);
    }
} 