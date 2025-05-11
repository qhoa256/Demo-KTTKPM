package com.costumeRental.costumeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${supplier-service.url:http://localhost:8081}")
    private String supplierServiceUrl;

    @Bean
    public WebClient supplierServiceWebClient() {
        return WebClient.builder()
                .baseUrl(supplierServiceUrl)
                .build();
    }
} 