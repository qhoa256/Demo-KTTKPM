-- Insert sample data into tblPayment
INSERT INTO tblPayment (payment_method, payment_note) VALUES 
('Cash', 'Payment received at store'),
('Credit Card', 'Visa ending with 4321'),
('Bank Transfer', 'Transfer from account 123456789'),
('PayPal', 'Payment ID: PAY-12345ABC'),
('Momo', 'Transaction ID: MOMO-67890'),
('Credit Card', 'MasterCard ending with 5678'),
('Momo', 'Transaction ID: MOMO-54321'),
('Bank Transfer', 'Transfer from account 987654321'),
('Cash', 'Cash on delivery'),
('PayPal', 'Payment ID: PAY-67890XYZ'),
('Cash', 'Payment received at store branch B'),
('Bank Transfer', 'Transfer from account 456789123 ref: INV-2023'),
('Momo', 'Transaction ID: MOMO-ABCDE123'),
('Credit Card', 'Visa ending with 8765'),
('PayPal', 'Payment ID: PAY-FGHIJ789');

-- Insert sample data into tblBill
INSERT INTO tblBill (customer_id, rent_date, return_date, payment_id, note, address) VALUES
(2, '2025-04-03', '2025-04-07', 8, 'Giao hàng trước 9h sáng ngày 03/04/2025 - Không giao hàng vào giờ nghỉ trưa', 'Quat Lam, Nam Dinh'),
(4, '2025-04-10', '2025-04-15', 3, 'Chuyển phát nhanh - Yêu cầu ký nhận và lưu ý đóng gói chống sốc', 'Muong Ang, Dien Bien'),
(5, '2025-05-12', '2025-05-18', 12, 'Giao hàng buổi tối từ 18h-21h - Liên hệ trước 30 phút khi đến', 'Quat Lam, Nam Dinh'),
(7, '2025-05-01', '2025-05-05', 6, 'Chỉ giao hàng vào thứ 7 và chủ nhật - Để hàng trước cổng khi vắng nhà', 'My Duc, Ha Noi'),
(9, '2025-04-22', '2025-04-27', 9, 'Giao hàng kèm hóa đơn VAT - Yêu cầu niêm phong bảo mật', 'Hoai Duc, Ha Noi'),
(6, '2025-05-20', '2025-05-25', 14, 'Không giao hàng khi trời mưa - Để lại tại bảo vệ tòa nhà', 'My Duc, Ha Noi'),
(8, '2025-04-15', '2025-04-20', 5, 'Giao hàng 2 lần kiểm tra - Yêu cầu chứng từ đầy đủ', 'Thai Binh, Hung Yen'),
(10, '2025-05-05', '2025-05-10', 11, 'Chuyển hàng siêu tốc trong 24h - Lưu ý hủy đơn nếu không giao được', 'Dong Cuong, Thanh Hoa'),
(4, '2025-04-18', '2025-04-23', 7, 'Chỉ nhận hàng tại công ty - Không giao hàng cá nhân', 'Muong Ang, Dien Bien'),
(5, '2025-05-25', '2025-05-30', 2, 'Giao hàng kèm phiếu bảo hành - Kiểm tra serial number khi nhận', 'Quat Lam, Nam Dinh'),
(2, '2025-04-28', '2025-05-02', 10, 'Yêu cầu giao hàng thử nghiệm - Báo cáo lỗi ngay khi phát hiện', 'Quat Lam, Nam Dinh'),
(7, '2025-05-08', '2025-05-13', 4, 'Giao hàng kèm tài liệu hướng dẫn - Ký xác nhận hồ sơ đầy đủ', 'My Duc, Ha Noi'),
(9, '2025-04-05', '2025-04-09', 13, 'Chuyển phát ưu tiên - Đảm bảo niêm phong nguyên vẹn', 'Hoai Duc, Ha Noi'),
(6, '2025-05-15', '2025-05-20', 1, 'Giao hàng không cần ký nhận - Để lại tại hòm thư', 'My Duc, Ha Noi'),
(10, '2025-04-30', '2025-05-05', 15, 'Yêu cầu giao hàng đúng chủng loại - Hoàn tiền nếu sai lệch', 'Dong Cuong, Thanh Hoa');