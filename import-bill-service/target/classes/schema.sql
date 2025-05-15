CREATE DATABASE IF NOT EXISTS import_bill_service_db;
USE import_bill_service_db;
-- Importing Bill tables
CREATE TABLE IF NOT EXISTS tblImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    managerId BIGINT NOT NULL,
    supplierId BIGINT NOT NULL,
    importDate DATE
);