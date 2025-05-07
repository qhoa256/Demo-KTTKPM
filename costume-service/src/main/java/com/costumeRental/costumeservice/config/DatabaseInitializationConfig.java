package com.costumeRental.costumeservice.config;

import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseInitializationConfig {

    @Bean
    public SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(
            DataSource dataSource,
            SqlInitializationProperties properties) {
        
        DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(properties.getSchemaLocations());
        settings.setDataLocations(properties.getDataLocations());
        settings.setContinueOnError(properties.isContinueOnError());
        settings.setSeparator(properties.getSeparator());
        settings.setEncoding(properties.getEncoding());
        settings.setMode(properties.getMode());
        
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, settings);
    }
} 