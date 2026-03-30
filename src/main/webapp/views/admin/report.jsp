<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Báo cáo quản trị REMS</title>

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
            <h2>Báo cáo</h2>

            <div class="filter-container">
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/reports">

                    <label>Từ ngày</label>
                    <input type="date"
                           name="fromDate"
                           value="${param.fromDate}">

                    <label>Đến ngày</label>
                    <input type="date"
                           name="toDate"
                           value="${param.toDate}">

                    <button type="submit">Lọc</button>

                    <a class="btn-export"
                       href="${pageContext.request.contextPath}/admin/reports?action=export">
                        Xuất CSV
                    </a>
                </form>
            </div>

            <div class="chart-container">
                <h3>Doanh thu theo tháng</h3>
                <canvas id="revenueChart"></canvas>
            </div>

            <div class="chart-container">
                <h3>Số giao dịch theo tháng</h3>
                <canvas id="transactionChart"></canvas>
            </div>

            <div class="transactions-container">
                <h3>Chi tiết doanh thu</h3>

                <table>
                    <thead>
                    <tr>
                        <th>Tháng</th>
                        <th>Doanh thu</th>
                        <th>Số giao dịch</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${reports}" var="r">
                        <tr>
                            <td>${r.month}</td>
                            <td class="money">${r.revenue}</td>
                            <td>${r.transactions}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    const revenueData = [
        <c:forEach items="${reports}" var="r" varStatus="loop">
        {
            month: "${r.month}",
            revenue: ${r.revenue != null ? r.revenue : 0},
            count: ${r.transactions != null ? r.transactions : 0}
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
</script>

<script src="${pageContext.request.contextPath}/assets/js/admin/report.js?v=1"></script>
</body>
</html>
