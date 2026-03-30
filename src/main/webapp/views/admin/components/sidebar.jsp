<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="sidebar">
    <div class="sidebar-logo">
        <h2>REMS</h2>
    </div>

    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/admin/dashboard">
                Bảng điều khiển
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/properties">
                Bất động sản
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/bookings">
                Đặt lịch
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/transactions">
                Giao dịch
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/users">
                Người dùng
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/reports">
                Báo cáo
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/activity-logs">
                Nhật ký hoạt động
            </a>
        </li>
    </ul>
</div>
