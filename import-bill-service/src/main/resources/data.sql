-- Insert User and Staff data
INSERT INTO tblUser (username, password, Discriminator) VALUES
('manager1', 'password', 'STAFF'),
('manager2', 'password', 'STAFF'),
('manager3', 'password', 'STAFF');

INSERT INTO Staff (id, position) VALUES
(1, 'Warehouse Manager'),
(2, 'Senior Manager'),
(3, 'Assistant Manager');

-- Insert Supplier data
INSERT INTO tblSupplier (name, email, contact, address) VALUES
('Costumes Inc.', 'contact@costumesinc.com', '123-456-7890', '123 Costume St, Costume City'),
('Theater Supplies', 'info@theatersupplies.com', '234-567-8901', '456 Theater Ave, Stage Town'),
('Fantasy Outfits', 'sales@fantasyoutfits.com', '345-678-9012', '789 Fantasy Blvd, Dream City');

-- Insert Costume data
INSERT INTO tblCostume (category, name, description, price) VALUES
('Historical', 'Medieval Knight', 'Complete knight costume with armor pieces', 150.00),
('Sci-Fi', 'Space Explorer', 'Futuristic space explorer outfit', 120.00),
('Fantasy', 'Wizard Robe', 'Mystical wizard robe with hat', 85.00),
('Historical', 'Victorian Dress', 'Elegant Victorian-era dress', 175.00),
('Modern', 'Business Suit', 'Professional business attire', 95.00);

-- Insert CostumeSupplier data
INSERT INTO tblCostumeSupplier (costumeId, supplierId) VALUES
(1, 1), -- Knight from Costumes Inc
(2, 3), -- Space Explorer from Fantasy Outfits
(3, 3), -- Wizard Robe from Fantasy Outfits
(4, 2), -- Victorian Dress from Theater Supplies
(5, 1); -- Business Suit from Costumes Inc

-- Insert ImportingBill data
INSERT INTO tblImportingBill (managerId, supplierId, importDate) VALUES
(3, 1, '2023-01-15'),
(3, 2, '2023-02-20'),
(3, 3, '2023-03-25');

-- Insert CostumeImportingBill data
INSERT INTO tblCostumeImportingBill (costumeSupplierID, importingBillId, importPrice, quantity, name, description) VALUES
(1, 1, 100.00, 5, 'Medieval Knight Batch', 'Bulk order of knight costumes'),
(5, 1, 70.00, 10, 'Business Suit Batch', 'Bulk order of business suits'),
(4, 2, 120.00, 3, 'Victorian Dress Collection', 'Limited Victorian dress collection'),
(2, 3, 90.00, 7, 'Space Explorer Set', 'New space explorer costume set'),
(3, 3, 60.00, 8, 'Wizard Robe Collection', 'Premium wizard robes'); 