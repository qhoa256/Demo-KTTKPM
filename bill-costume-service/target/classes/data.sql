-- Insert sample data into tblPayment
INSERT INTO tblPayment (payment_method, payment_note) VALUES 
('Cash', 'Payment received at store'),
('Credit Card', 'Visa ending with 4321'),
('Bank Transfer', 'Transfer from account 123456789'),
('PayPal', 'Payment ID: PAY-12345ABC'),
('Momo', 'Transaction ID: MOMO-67890');

-- Insert sample data into tblBill
INSERT INTO tblBill (customer_id, rent_date, return_date, payment_id) VALUES 
(2, '2025-05-01', '2025-05-05', 1),
(2, '2025-05-10', '2025-05-15', 2),
(2, '2025-05-15', '2025-05-18', 3),
(2, '2025-05-20', '2025-05-25', 4),
(2, '2025-05-25', '2025-05-30', 5),
(2, '2025-05-01', '2025-05-05', 1),
(2, '2025-05-10', '2025-05-15', 2),
(2, '2025-05-15', '2025-05-18', 3);