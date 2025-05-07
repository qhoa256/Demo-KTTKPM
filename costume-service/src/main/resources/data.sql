-- Thêm dữ liệu mẫu cho bảng tblCostume
INSERT INTO tblCostume (category, name, description, price) VALUES ('Trang phục truyền thống', 'Áo dài truyền thống', 'Áo dài truyền thống màu đỏ size M', 150000);
INSERT INTO tblCostume (category, name, description, price) VALUES ('Trang phục hiện đại', 'Vest công sở', 'Vest công sở màu đen size L', 200000);
INSERT INTO tblCostume (category, name, description, price) VALUES ('Trang phục lịch sử', 'Trang phục hoàng gia', 'Trang phục hoàng gia màu vàng size XL', 300000);
INSERT INTO tblCostume (category, name, description, price) VALUES ('Trang phục thể thao', 'Đồng phục thể thao', 'Đồng phục thể thao màu xanh size S', 100000);
INSERT INTO tblCostume (category, name, description, price) VALUES ('Trang phục dạ hội', 'Đầm dạ hội', 'Đầm dạ hội màu bạc size M', 500000);

-- Thêm dữ liệu mẫu cho bảng tblCostumeBill
INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, description) 
VALUES (1, 150000, 'BILL-001', 2, 'Áo dài truyền thống', 'Áo dài màu đỏ truyền thống');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, description) 
VALUES (2, 200000, 'BILL-001', 1, 'Vest công sở', 'Vest đen công sở nam');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, description) 
VALUES (3, 300000, 'BILL-002', 1, 'Trang phục hoàng gia', 'Trang phục hoàng gia cổ điển');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, description) 
VALUES (4, 100000, 'BILL-003', 3, 'Đồng phục thể thao', 'Bộ đồng phục thể thao đội tuyển');

INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, description) 
VALUES (5, 500000, 'BILL-004', 1, 'Đầm dạ hội', 'Đầm dự tiệc màu bạc sang trọng');

-- Thêm dữ liệu mẫu cho bảng tblCostumeImportingBill
INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) 
VALUES (1, 100000, 10, 'Áo dài truyền thống', 'Nhập lô áo dài mới cho mùa Tết');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) 
VALUES (2, 150000, 5, 'Vest công sở', 'Nhập bổ sung vest đen cho thuê');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) 
VALUES (3, 250000, 3, 'Trang phục hoàng gia', 'Nhập từ nhà thiết kế cao cấp');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) 
VALUES (4, 80000, 15, 'Đồng phục thể thao', 'Nhập từ xưởng may cho mùa giải mới');

INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) 
VALUES (5, 400000, 2, 'Đầm dạ hội', 'Nhập theo đơn đặt hàng dự tiệc'); 