-- Insert sample addresses
INSERT INTO Address (id, street, city) VALUES 
(1, 'so nha 22 pho Cao', 'Hung Yen'),
(2, 'duong Quat Lam', 'Nam Dinh'),
(3, '217 Tran Phu', 'Ha Noi');

-- Insert sample full names
INSERT INTO FullName (id, firstName, lastName) VALUES 
(1, 'Hieu', 'Nguyen Huu'),
(2, 'Anh', 'Nguyen Tien'),
(3, 'Hoa', 'Nguyen Huu Quang');

-- Insert sample users
INSERT INTO tblUser (id, username, password, AddressId, FullNameId, Discriminator) VALUES 
(1, 'user', '1', 1, 1, 'USER'),
(2, 'customer', '1', 2, 2, 'CUSTOMER'),
(3, 'admin', '1', 3, 3, 'STAFF');

-- Insert sample customer
INSERT INTO Customer (id, loyaltyPoints) VALUES 
(2, 100);

-- Insert sample staff
INSERT INTO Staff (id, position) VALUES 
(3, 'Manager'); 