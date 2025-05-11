-- Drop tables if they exist (in reverse order due to foreign key constraints)
DROP TABLE IF EXISTS tblCostumeImportingBill;
DROP TABLE IF EXISTS tblCostumeBill;
DROP TABLE IF EXISTS tblCostumeSupplier;
DROP TABLE IF EXISTS tblSupplier;
DROP TABLE IF EXISTS tblCostume;

-- Create tables
CREATE TABLE IF NOT EXISTS tblCostume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    price DECIMAL(19,2)
);

CREATE TABLE IF NOT EXISTS tblCostumeBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    rent_price DECIMAL(19,2),
    bill_id VARCHAR(255),
    quantity INT,
    name VARCHAR(255),
    description VARCHAR(255),
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id)
);

CREATE TABLE IF NOT EXISTS tblSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    contact VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tblCostumeSupplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    supplier_id BIGINT,
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id),
    FOREIGN KEY (supplier_id) REFERENCES tblSupplier(id)
);

CREATE TABLE IF NOT EXISTS tblCostumeImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_supplier_id BIGINT,
    import_price DECIMAL(19,2),
    quantity INT,
    name VARCHAR(255),
    description VARCHAR(255),
    FOREIGN KEY (costume_supplier_id) REFERENCES tblCostumeSupplier(id)
); 