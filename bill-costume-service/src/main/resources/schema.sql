CREATE DATABASE IF NOT EXISTS bill_costume_service_db;
USE bill_costume_service_db;
-- Disable foreign key checks to allow dropping tables with foreign key constraints
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tblBill;
DROP TABLE IF EXISTS tblPayment;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=1;

-- Create Payment table
CREATE TABLE IF NOT EXISTS tblPayment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_method VARCHAR(100),
    payment_note VARCHAR(255)
);

-- Create Bill table with foreign key to Payment
CREATE TABLE IF NOT EXISTS tblBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    rent_date DATE,
    return_date DATE,
    payment_id BIGINT,
    note VARCHAR(255),
    address VARCHAR(255),
    FOREIGN KEY (payment_id) REFERENCES tblPayment(id) ON DELETE SET NULL
); 