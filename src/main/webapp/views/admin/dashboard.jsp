<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Bảng điều khiển quản trị REMS</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>
<div class="dashboard-container">
    <jsp:include page="components/sidebar.jsp"/>

    <div class="main-content">
        <jsp:include page="components/topbar.jsp"/>

        <div class="dashboard-content">
            <div class="cards-container">
                <c:forEach items="${dashboard.cards}" var="card">
                    <div class="card">
                        <div class="card-title">${card.label}</div>
                        <div class="card-value money">${card.value}</div>
                    </div>
                </c:forEach>
            </div>

            <div class="property-status">
                <h3>Trạng thái bất động sản</h3>

                <div class="status-grid">
                    <div class="status-card">
                        <div class="status-title">BẢN NHÁP</div>
                        <div class="status-value">${dashboard.propertyStats['DRAFT']}</div>
                    </div>
                    <div class="status-card">
                        <div class="status-title">ĐANG MỞ BÁN/THUÊ</div>
                        <div class="status-value">${dashboard.propertyStats['AVAILABLE']}</div>
                    </div>
                    <div class="status-card">
                        <div class="status-title">ĐÃ GIỮ CHỖ</div>
                        <div class="status-value">${dashboard.propertyStats['RESERVED']}</div>
                    </div>
                    <div class="status-card">
                        <div class="status-title">ĐÃ BÁN</div>
                        <div class="status-value">${dashboard.propertyStats['SOLD']}</div>
                    </div>
                    <div class="status-card">
                        <div class="status-title">ĐÃ CHO THUÊ</div>
                        <div class="status-value">${dashboard.propertyStats['RENTED']}</div>
                    </div>
                    <div class="status-card">
                        <div class="status-title">NGỪNG HIỂN THỊ</div>
                        <div class="status-value">${dashboard.propertyStats['INACTIVE']}</div>
                    </div>
                </div>
            </div>

            <div class="transaction-status">
                <h3>Trạng thái giao dịch</h3>

                <div class="status-grid">
                    <div class="status-card pending">
                        <div class="status-title">CHỜ XỬ LÝ</div>
                        <div class="status-value">${dashboard.transactionStats['PENDING']}</div>
                    </div>
                    <div class="status-card completed">
                        <div class="status-title">HOÀN TẤT</div>
                        <div class="status-value">${dashboard.transactionStats['COMPLETED']}</div>
                    </div>
                    <div class="status-card failed">
                        <div class="status-title">THẤT BẠI</div>
                        <div class="status-value">${dashboard.transactionStats['FAILED']}</div>
                    </div>
                    <div class="status-card cancelled">
                        <div class="status-title">ĐÃ HỦY</div>
                        <div class="status-value">${dashboard.transactionStats['CANCELLED']}</div>
                    </div>
                </div>
            </div>

            <c:if test="${dashboard.pendingTransactions > 0}">
                <div class="alert-box">
                    ${dashboard.pendingTransactions} giao dịch đang chờ xử lý
                    <a href="${pageContext.request.contextPath}/admin/transactions?status=PENDING">Xem ngay</a>
                </div>
            </c:if>

            <c:if test="${dashboard.draftProperties > 0}">
                <div class="alert-box">
                    ${dashboard.draftProperties} bất động sản đang chờ duyệt
                    <a href="${pageContext.request.contextPath}/admin/properties?status=DRAFT">Xem ngay</a>
                </div>
            </c:if>

            <c:if test="${dashboard.reservedTooLong > 0}">
                <div class="alert-box">
                    ${dashboard.reservedTooLong} bất động sản bị giữ chỗ quá lâu
                    <a href="${pageContext.request.contextPath}/admin/properties?status=RESERVED">Xem ngay</a>
                </div>
            </c:if>

            <div class="chart-container">
                <h3>Doanh thu theo tháng</h3>
                <canvas id="revenueChart"></canvas>
            </div>

            <div class="transactions-container">
                <h3>Giao dịch gần đây</h3>

                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Bất động sản</th>
                        <th>Khách hàng</th>
                        <th>Số tiền</th>
                        <th>Trạng thái</th>
                        <th>Ngày</th>
                        <th>Người xử lý</th>
                        <th>Loại</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${dashboard.recentTransactions}" var="tx">
                        <tr>
                            <td>${tx.transactionId}</td>
                            <td>${tx.propertyTitle}</td>
                            <td>${tx.customerName}</td>
                            <td class="money">${tx.amount}</td>
                            <td>
                                <span class="badge
                                <c:choose>
                                    <c:when test="${tx.status == 'COMPLETED'}">badge-completed</c:when>
                                    <c:when test="${tx.status == 'PENDING'}">badge-pending</c:when>
                                    <c:otherwise>badge-failed</c:otherwise>
                                </c:choose>
                                ">
                                    ${tx.status}
                                </span>
                            </td>
                            <td>${tx.date}</td>
                            <td>${tx.processedBy}</td>
                            <td>${tx.type}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    const revenueLabels = [
        <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
        "${p.month}"${!loop.last ? "," : ""}
        </c:forEach>
    ];

    const revenueData = [
        <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
        ${p.revenue}${!loop.last ? "," : ""}
        </c:forEach>
    ];
</script>

<script src="${pageContext.request.contextPath}/assets/js/admin/dashboard.js?v=1"></script>
</body>
</html>
