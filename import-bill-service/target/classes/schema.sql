-- User tables
CREATE TABLE IF NOT EXISTS tblUser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    AddressId BIGINT,
    FullNameId BIGINT,
    Discriminator VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS FullName (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Staff (
    id BIGINT PRIMARY KEY,
    position VARCHAR(255),
    FOREIGN KEY (id) REFERENCES tblUser(id)
);

-- Supplier and Costume tables
CREATE TABLE IF NOT EXISTS tblSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    contact VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tblCostume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS tblCostumeSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costumeId BIGINT NOT NULL,
    supplierId BIGINT NOT NULL,
    FOREIGN KEY (costumeId) REFERENCES tblCostume(id),
    FOREIGN KEY (supplierId) REFERENCES tblSupplier(id)
);

-- Importing Bill tables
CREATE TABLE IF NOT EXISTS tblImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    managerId BIGINT NOT NULL,
    supplierId BIGINT NOT NULL,
    importDate DATE,
    FOREIGN KEY (managerId) REFERENCES Staff(id),
    FOREIGN KEY (supplierId) REFERENCES tblSupplier(id)
);

CREATE TABLE IF NOT EXISTS tblCostumeImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costumeSupplierID BIGINT NOT NULL,
    importingBillId BIGINT NOT NULL,
    importPrice DECIMAL(10, 2),
    quantity INT,
    name VARCHAR(255),
    description TEXT,
    FOREIGN KEY (costumeSupplierID) REFERENCES tblCostumeSupplier(id),
    FOREIGN KEY (importingBillId) REFERENCES tblImportingBill(id)
); 