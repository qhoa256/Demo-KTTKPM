-- Drop the database if it exists
DROP DATABASE IF EXISTS costume_service_db;

-- Create the database
CREATE DATABASE costume_service_db;

-- Use the database
USE costume_service_db;

-- Create tables
CREATE TABLE tblCostume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    price DECIMAL(19,2)
);

CREATE TABLE tblCostumeBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    rent_price DECIMAL(19,2),
    bill_id VARCHAR(255),
    quantity INT,
    name VARCHAR(255),
    description VARCHAR(255),
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id)
);

CREATE TABLE tblSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    contact VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE tblCostumeSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    supplier_id BIGINT,
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id),
    FOREIGN KEY (supplier_id) REFERENCES tblSupplier(id)
);

CREATE TABLE tblCostumeImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_supplier_id BIGINT,
    import_price DECIMAL(19,2),
    quantity INT,
    name VARCHAR(255),
    description VARCHAR(255),
    FOREIGN KEY (costume_supplier_id) REFERENCES tblCostumeSupplier(id)
); 