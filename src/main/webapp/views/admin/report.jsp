<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

    <title>REMS Admin Reports</title>

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

            <h2>Reports</h2>

            <div class="filter-container">

                <form method="get"
                      action="${pageContext.request.contextPath}/admin/reports">

                    <label>From</label>

                    <input type="date"
                           name="fromDate"
                           value="${param.fromDate}">

                    <label>To</label>

                    <input type="date"
                           name="toDate"
                           value="${param.toDate}">

                    <button type="submit">Filter</button>

                    <a class="btn-export"
                       href="${pageContext.request.contextPath}/admin/reports?action=export">
                        Export CSV
                    </a>

                </form>

            </div>

            <!-- REVENUE CHART -->

            <div class="chart-container">

                <h3>Monthly Revenue</h3>

                <canvas id="revenueChart"></canvas>

            </div>

            <!-- TRANSACTION CHART -->

            <div class="chart-container">

                <h3>Transactions Per Month</h3>

                <canvas id="transactionChart"></canvas>

            </div>

            <!-- TABLE -->

            <div class="transactions-container">

                <h3>Revenue Details</h3>

                <table>

                    <thead>

                    <tr>
                        <th>Month</th>
                        <th>Revenue</th>
                        <th>Transactions</th>
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