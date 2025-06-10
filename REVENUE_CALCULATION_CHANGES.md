# Tóm tắt thay đổi Logic tính Revenue trong Module Thống kê

## Tổng quan
Đã cập nhật logic tính revenue trong `BaseStatisticsService.java` để tính revenue theo tỷ lệ thời gian thực tế sử dụng thay vì tính toàn bộ giá thuê.

## Thay đổi chính

### 1. Method `getBillsFromBillService()` - Filter logic mới
**Trước:**
- Chỉ filter theo `rentDate` (ngày bắt đầu thuê)
- Lấy tất cả hóa đơn có ngày thuê trong khoảng filter

**Bây giờ:**
- Filter theo **overlap** giữa rental period và filter range
- Lấy hóa đơn có khoảng thời gian thuê overlap với khoảng filter
- Điều kiện: `rentDate <= filterEndDate` AND `returnDate >= filterStartDate`

### 2. Method `calculateProportionalRevenue()` - Logic tính revenue mới
**Công thức:**
```
effectiveStartDate = max(rentDate, filterStartDate)
effectiveEndDate = min(returnDate, filterEndDate)
totalRentalDays = returnDate - rentDate + 1
overlapDays = effectiveEndDate - effectiveStartDate + 1
dailyRate = rentPrice / totalRentalDays
proportionalRevenue = dailyRate × overlapDays × quantity
```

**Ví dụ:**
- Thuê từ 01/01/2025 đến 10/01/2025 (10 ngày), giá 1,000,000 VND
- Filter từ 05/01/2025 đến 15/01/2025
- Overlap: 05/01 đến 10/01 (6 ngày)
- Daily rate: 1,000,000 / 10 = 100,000 VND/ngày
- Revenue: 100,000 × 6 = 600,000 VND

### 3. Áp dụng trong các method chính

#### `getRevenueByCategoryWithDateRange()`
- Tính revenue theo category với logic tỷ lệ
- Tìm hóa đơn tương ứng để lấy ngày thuê/trả
- Áp dụng `calculateProportionalRevenue()` cho mỗi costume bill

#### `getCostumesByCategory()`
- Tính revenue cho từng trang phục trong category
- Cộng dồn revenue theo tỷ lệ cho mỗi trang phục

#### `getBillsByCostume()`
- Tính revenue cho hóa đơn cụ thể với trang phục cụ thể
- Áp dụng filter revenue theo tỷ lệ

## Lợi ích

1. **Chính xác hơn:** Revenue phản ánh đúng thời gian sử dụng thực tế
2. **Công bằng:** Không tính revenue cho thời gian ngoài khoảng filter
3. **Linh hoạt:** Xử lý được các trường hợp overlap phức tạp
4. **Robust:** Có fallback mechanism khi thiếu dữ liệu

## Xử lý edge cases

1. **Chưa trả trang phục (returnDate = null):**
   - Sử dụng filterEndDate hoặc ngày hiện tại

2. **Không tìm thấy hóa đơn:**
   - Fallback về cách tính cũ

3. **Không có overlap:**
   - Revenue = 0

4. **Lỗi parse ngày:**
   - Loại bỏ hóa đơn hoặc fallback

## Debug logging
Thêm logging chi tiết để theo dõi quá trình tính toán:
- Thông tin trang phục
- Ngày thuê/trả
- Khoảng filter và effective range
- Số ngày và daily rate
- Revenue cuối cùng

## Deployment
- Đã build lại Docker images
- Đã deploy lên Kubernetes
- Tất cả services đang chạy ổn định
