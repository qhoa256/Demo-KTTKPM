CREATE DATABASE IF NOT EXISTS user_service_db;
USE user_service_db;
-- Insert sample addresses
INSERT INTO Address (id, street, city) VALUES 
(1, 'Phu Cu', 'Hung Yen'),
(2, 'Quat Lam', 'Nam Dinh'),
(3, 'Phu Cu', 'Hung Yen'),
(4, 'Muong Ang', 'Dien Bien'),
(5, 'Quat Lam', 'Nam Dinh'),
(6, 'My Duc', 'Ha Noi'),
(7, 'My Duc', 'Ha Noi'),
(8, 'Thai Binh', 'Hung Yen'),
(9, 'Hoai Duc', 'Ha Noi'),
(10, 'Dong Cuong', 'Thanh Hoa');

-- Insert sample full names
INSERT INTO FullName (id, firstName, lastName) VALUES 
(1, 'User', 'Nguyen Huu Quang'),
(2, 'Customer', 'Nguyen Huu Quang'),
(3, 'Admin', 'Nguyen Huu Quang'),
(4, 'Duong', 'Lo Van'),
(5, 'Anh', 'Nguyen Tien'),
(6, 'Dat', 'Tran Quy'),
(7, 'Lan', 'Nguyen Thi'),
(8, 'Manh', 'Nguyen Dinh'),
(9, 'Nam', 'Nguyen Viet'),
(10, 'Hai', 'Nguyen Hoang');

-- Insert sample users
INSERT INTO tblUser (id, username, password, AddressId, FullNameId, Discriminator) VALUES 
(1, 'user', '1', 1, 1, 'USER'),
(2, 'customer', '1', 2, 2, 'CUSTOMER'),
(3, 'admin', '1', 3, 3, 'STAFF'),
(4, 'duonglv', '1', 4, 4, 'CUSTOMER'),
(5, 'anhnt', '1', 5, 5, 'CUSTOMER'),
(6, 'dattq', '1', 6, 6, 'CUSTOMER'),
(7, 'lannt', '1', 7, 7, 'CUSTOMER'),
(8, 'manhnd', '1', 8, 8, 'CUSTOMER'),
(9, 'namnv', '1', 9, 9, 'CUSTOMER'),
(10, 'hainh', '1', 10, 10, 'CUSTOMER');

-- Insert sample customer
INSERT INTO Customer (id, loyaltyPoints) VALUES 
(2, 100),
(4, 200),
(5, 300),
(6, 400),
(7, 500),
(8, 600),
(9, 700),
(10, 800);

-- Insert sample staff
INSERT INTO Staff (id, position) VALUES 
(3, 'Manager'); 