-- Drop tables if they exist to avoid errors when schema is re-run
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Staff;
DROP TABLE IF EXISTS tblUser;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS FullName;

-- Create Address table
CREATE TABLE Address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(255)
);

-- Create FullName table
CREATE TABLE FullName (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255)
);

-- Create tblUser table
CREATE TABLE tblUser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    AddressId BIGINT,
    FullNameId BIGINT,
    Discriminator VARCHAR(50),
    FOREIGN KEY (AddressId) REFERENCES Address(id),
    FOREIGN KEY (FullNameId) REFERENCES FullName(id)
);

-- Create Staff table
CREATE TABLE Staff (
    id BIGINT PRIMARY KEY,
    position VARCHAR(255),
    FOREIGN KEY (id) REFERENCES tblUser(id)
);

-- Create Customer table
CREATE TABLE Customer (
    id BIGINT PRIMARY KEY,
    loyaltyPoints INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES tblUser(id)
); 