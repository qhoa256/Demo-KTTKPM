-- Insert sample addresses
INSERT INTO Address (id, street, city) VALUES 
(1, '123 Main St', 'New York'),
(2, '456 Elm St', 'Los Angeles'),
(3, '789 Oak St', 'Chicago');

-- Insert sample full names
INSERT INTO FullName (id, firstName, lastName) VALUES 
(1, 'John', 'Doe'),
(2, 'Jane', 'Smith'),
(3, 'Admin', 'User');

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