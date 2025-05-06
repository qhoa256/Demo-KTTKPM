CREATE TABLE IF NOT EXISTS tblImportingBill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    managerId BIGINT NOT NULL,
    supplierId BIGINT NOT NULL,
    importDate DATE
); 