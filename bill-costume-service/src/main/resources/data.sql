-- Insert sample data into tblPayment
INSERT INTO tblPayment (payment_method, payment_note) VALUES 
('Cash', 'Payment received at store'),
('Credit Card', 'Visa ending with 4321'),
('Bank Transfer', 'Transfer from account 123456789'),
('PayPal', 'Payment ID: PAY-12345ABC'),
('Momo', 'Transaction ID: MOMO-67890');

-- Insert sample data into tblBill
INSERT INTO tblBill (customer_id, rent_date, return_date, payment_id) VALUES 
(1, '2023-10-01', '2023-10-05', 1),
(2, '2023-10-10', '2023-10-15', 2),
(3, '2023-10-15', '2023-10-18', 3),
(4, '2023-10-20', '2023-10-25', 4),
(5, '2023-10-25', '2023-10-30', 5),
(6, '2023-11-01', '2023-11-05', 1),
(7, '2023-11-10', '2023-11-15', 2),
(8, '2023-11-15', NULL, 3); -- Ongoing rental without return date 