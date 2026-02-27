# 🏢 REMS – Real Estate Management System

---

## 📌 1. Giới thiệu

**REMS (Real Estate Management System)** là hệ thống quản lý bất động sản được xây dựng theo kiến trúc module hóa, đảm bảo:

* 🔒 Bảo mật
* ⚡ Hiệu năng
* 🧩 Dễ mở rộng
* 🏗 Chuẩn Clean Architecture

---

# 📦 MODULE A – QUẢN LÝ BẤT ĐỘNG SẢN

## 🎯 Chức năng

Quản lý thông tin bất động sản.

## 📜 Business Rules

* Chỉ **Admin** & **Staff** được tạo / chỉnh sửa BĐS
* Không xóa vật lý → sử dụng **Soft Delete** (`status = INACTIVE`)

### 📊 Trạng thái BĐS

| Status    | Ý nghĩa          |
| --------- | ---------------- |
| AVAILABLE | Có thể giao dịch |
| PENDING   | Đang chờ xử lý   |
| SOLD      | Đã bán           |
| INACTIVE  | Ngừng hiển thị   |

### 💰 Quy định

* Giá phải **> 0**
* Có phân trang khi tìm kiếm
* Upload tối đa **5 ảnh**
* Mỗi ảnh ≤ **5MB**
* Tên file ảnh được random hóa

---

# 👤 MODULE B – USER & OTP

## 📜 Business Rules

* Password được mã hóa bằng **BCrypt**
* 1 User chỉ có **1 Role**
* User phải verify email trước khi giao dịch

### 🔐 OTP Rules

* Hết hạn sau **5 phút**
* Tối đa **3 lần nhập sai**
* OTP được **hash trong database**

---

# 📖 MODULE C – LỊCH SỬ XEM BĐS

## 📌 Lưu trữ thông tin

* `user_id`
* `property_id`
* `viewed_at`
* `ip_address`

## 🗂 Chính sách lưu trữ

* Log được giữ trong **6 tháng**

---

# 💳 MODULE D – GIAO DỊCH

## 📊 Trạng thái giao dịch

| Status    | Ý nghĩa           |
| --------- | ----------------- |
| PENDING   | Đang chờ xác nhận |
| CONFIRMED | Đã xác nhận       |
| COMPLETED | Hoàn tất          |
| CANCELLED | Đã hủy            |

## 📜 Business Rules

* Khi tạo giao dịch → Property chuyển sang **PENDING**
* Khi COMPLETED → Property chuyển sang **SOLD**
* Khi CANCELLED → Property chuyển lại **AVAILABLE**
* Sử dụng **Database Transaction** để tránh double booking

---

# 📊 MODULE E – BÁO CÁO

## 📌 Điều kiện tính toán

* Chỉ tính giao dịch có trạng thái **COMPLETED**

## 🔎 Bộ lọc

* Theo **Tháng**
* Theo **Quý**
* Theo **Năm**

## 📤 Export

* Hỗ trợ xuất file **CSV**

---

# 🏗 II. Thiết kế kiến trúc

```
rems-system
│
├── pom.xml
│
└── src
    └── main
        ├── java
        │   └── com.rems
        │       ├── common
        │       ├── auth
        │       ├── user
        │       ├── property
        │       ├── transaction
        │       ├── history
        │       └── report
        │
        ├── resources
        │   └── application.properties
        │
        └── webapp
            ├── assets
            ├── views
            └── WEB-INF
                └── web.xml
```

---

# 🎯 Kiến trúc & Nguyên tắc

* Phân tách module rõ ràng
* Áp dụng nguyên tắc **Separation of Concerns**
* Sử dụng Transaction đảm bảo toàn vẹn dữ liệu
* Bảo mật theo chuẩn mã hóa & hash
* Dễ dàng mở rộng thêm module mới

---

# 🚀 Trạng thái dự án

> ✅ Đã hoàn thành phân tích Business Rules
>
> 🔄 Sẵn sàng bước vào giai đoạn Implementation

---

**Author:** REMS Development Team
