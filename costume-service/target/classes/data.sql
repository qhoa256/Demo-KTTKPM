-- Thêm dữ liệu mẫu cho bảng tblCostume
INSERT INTO tblCostume (category) VALUES ('Trang phục truyền thống');
INSERT INTO tblCostume (category) VALUES ('Trang phục hiện đại');
INSERT INTO tblCostume (category) VALUES ('Trang phục lịch sử');
INSERT INTO tblCostume (category) VALUES ('Trang phục thể thao');
INSERT INTO tblCostume (category) VALUES ('Trang phục dạ hội');

-- Thêm dữ liệu mẫu cho bảng tblCostumeBill
INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) 
VALUES (1, 150000, 'BILL-001', 2, 'Áo dài truyền thống', 'Đỏ', 'M');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) 
VALUES (2, 200000, 'BILL-001', 1, 'Vest công sở', 'Đen', 'L');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) 
VALUES (3, 300000, 'BILL-002', 1, 'Trang phục hoàng gia', 'Vàng', 'XL');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) 
VALUES (4, 100000, 'BILL-003', 3, 'Đồng phục thể thao', 'Xanh', 'S');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) 
VALUES (5, 500000, 'BILL-004', 1, 'Đầm dạ hội', 'Bạc', 'M');

-- Thêm dữ liệu mẫu cho bảng tblCostumeImportingBill
INSERT INTO tblCostumeImportingBill (costume_id, import_price, note, importing_bill_id, quantity, name, color, size) 
VALUES (1, 100000, 'Nhập mới', 'IMPORT-001', 10, 'Áo dài truyền thống', 'Đỏ', 'M');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, note, importing_bill_id, quantity, name, color, size) 
VALUES (2, 150000, 'Nhập mới', 'IMPORT-001', 5, 'Vest công sở', 'Đen', 'L');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, note, importing_bill_id, quantity, name, color, size) 
VALUES (3, 250000, 'Nhập từ nhà thiết kế', 'IMPORT-002', 3, 'Trang phục hoàng gia', 'Vàng', 'XL');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, note, importing_bill_id, quantity, name, color, size) 
VALUES (4, 80000, 'Nhập từ xưởng may', 'IMPORT-003', 15, 'Đồng phục thể thao', 'Xanh', 'S');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, note, importing_bill_id, quantity, name, color, size) 
VALUES (5, 400000, 'Nhập theo đơn đặt hàng', 'IMPORT-004', 2, 'Đầm dạ hội', 'Bạc', 'M'); 