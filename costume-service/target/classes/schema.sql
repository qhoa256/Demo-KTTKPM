-- Create tables
CREATE TABLE IF NOT EXISTS tblCostume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tblCostumeBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    rent_price DECIMAL(19,2),
    bill_id VARCHAR(255),
    quantity INT,
    name VARCHAR(255),
    color VARCHAR(255),
    size VARCHAR(255),
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id)
);

CREATE TABLE IF NOT EXISTS tblCostumeImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    costume_id BIGINT,
    import_price DECIMAL(19,2),
    note VARCHAR(255),
    importing_bill_id VARCHAR(255),
    quantity INT,
    name VARCHAR(255),
    color VARCHAR(255),
    size VARCHAR(255),
    FOREIGN KEY (costume_id) REFERENCES tblCostume(id)
); 