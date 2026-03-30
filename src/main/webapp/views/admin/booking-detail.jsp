<h2>Chi tiết đặt lịch</h2>

<div>
    <p><b>ID:</b> ${booking.bookingId}</p>
    <p><b>Trạng thái:</b> ${booking.status}</p>
    <p><b>Ngày tạo:</b> ${booking.createdAt}</p>
    <p><b>Thời điểm chấp nhận:</b> ${booking.acceptedAt}</p>
</div>

<hr>

<h3>Bất động sản</h3>

<p><b>Tiêu đề:</b> ${booking.propertyTitle}</p>
<p><b>Địa chỉ:</b> ${booking.propertyAddress}</p>

<hr>

<h3>Khách hàng</h3>

<p><b>Họ tên:</b> ${booking.customerName}</p>
<p><b>Email:</b> ${booking.customerEmail}</p>

<hr>

<h3>Ghi chú</h3>

<p>${booking.note}</p>

<hr>

<h3>Người xử lý</h3>

<p>${booking.staffName}</p>
