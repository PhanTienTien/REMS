<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>REMS Admin Dashboard</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/dashboard.css">

  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

</head>

<body>

<div class="dashboard-container">

  <!-- SIDEBAR -->
  <jsp:include page="components/sidebar.jsp"/>

  <div class="main-content">

    <!-- TOPBAR -->
    <jsp:include page="components/topbar.jsp"/>

    <div class="dashboard-content">

      <!-- DASHBOARD CARDS -->
      <div class="cards-container">

        <c:forEach items="${dashboard.cards}" var="card">

          <div class="card">

            <div class="card-title">
                ${card.label}
            </div>

            <div class="card-value money">
                ${card.value}
            </div>

          </div>

        </c:forEach>

      </div>

      <div class="property-status">

        <h3>Property Status</h3>

        <div class="status-grid">

          <div class="status-card">
            <div class="status-title">DRAFT</div>
            <div class="status-value">${dashboard.propertyStats['DRAFT']}</div>
          </div>

          <div class="status-card">
            <div class="status-title">AVAILABLE</div>
            <div class="status-value">${dashboard.propertyStats['AVAILABLE']}</div>
          </div>

          <div class="status-card">
            <div class="status-title">RESERVED</div>
            <div class="status-value">${dashboard.propertyStats['RESERVED']}</div>
          </div>

          <div class="status-card">
            <div class="status-title">SOLD</div>
            <div class="status-value">${dashboard.propertyStats['SOLD']}</div>
          </div>

          <div class="status-card">
            <div class="status-title">RENTED</div>
            <div class="status-value">${dashboard.propertyStats['RENTED']}</div>
          </div>

          <div class="status-card">
            <div class="status-title">INACTIVE</div>
            <div class="status-value">${dashboard.propertyStats['INACTIVE']}</div>
          </div>

        </div>

      </div>

      <div class="transaction-status">

        <h3>Transaction Status</h3>

        <div class="status-grid">

          <div class="status-card">
            <div class="status-title">PENDING</div>
            <div class="status-value">
              ${dashboard.transactionStats['PENDING']}
            </div>
          </div>

          <div class="status-card">
            <div class="status-title">COMPLETED</div>
            <div class="status-value">
              ${dashboard.transactionStats['COMPLETED']}
            </div>
          </div>

          <div class="status-card">
            <div class="status-title">FAILED</div>
            <div class="status-value">
              ${dashboard.transactionStats['FAILED']}
            </div>
          </div>

          <div class="status-card">
            <div class="status-title">CANCELLED</div>
            <div class="status-value">
              ${dashboard.transactionStats['CANCELLED']}
            </div>
          </div>

        </div>

      </div>

      <c:if test="${dashboard.pendingTransactions > 0}">
        <div class="alert-box">

          ⚠ ${dashboard.pendingTransactions} transactions waiting for processing

          <a href="${pageContext.request.contextPath}/admin/transactions?status=PENDING">
            Review now
          </a>

        </div>
      </c:if>

      <c:if test="${dashboard.draftProperties > 0}">
        <div class="alert-box">

          ⚠ ${dashboard.draftProperties} properties waiting for approval

          <a href="${pageContext.request.contextPath}/admin/properties?status=DRAFT">
            Review now
          </a>

        </div>
      </c:if>

      <c:if test="${dashboard.reservedTooLong > 0}">
        <div class="alert-box">

          ⚠ ${dashboard.reservedTooLong} properties reserved too long

          <a href="${pageContext.request.contextPath}/admin/properties?status=RESERVED">
            Review now
          </a>

        </div>
      </c:if>



      <!-- REVENUE CHART -->
      <div class="chart-container">

        <h3>Monthly Revenue</h3>

        <canvas id="revenueChart"></canvas>

      </div>


      <!-- RECENT TRANSACTIONS -->
      <div class="transactions-container">

        <h3>Recent Transactions</h3>

        <table>

          <thead>
          <tr>
            <th>ID</th>
            <th>Property</th>
            <th>Customer</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Date</th>
            <th>Processed By</th>
            <th>Type</th>
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


<!-- ========================= -->
<!-- DASHBOARD SCRIPT -->
<!-- ========================= -->

<script>

  document.addEventListener("DOMContentLoaded", function () {

    /* =========================
       FORMAT MONEY
    ========================== */

    document.querySelectorAll(".money").forEach(el => {

      let value = parseFloat(el.innerText);

      if (!isNaN(value)) {

        el.innerText =
                new Intl.NumberFormat('vi-VN', {
                  style: 'currency',
                  currency: 'VND'
                }).format(value);

      }

    });


    /* =========================
       BUILD REVENUE CHART DATA
    ========================== */

    const labels = [
      <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
      "${p.month}"${!loop.last ? "," : ""}
      </c:forEach>
    ];

    const data = [
      <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
      ${p.revenue}${!loop.last ? "," : ""}
      </c:forEach>
    ];


    /* =========================
       RENDER CHART
    ========================== */

    const ctx = document.getElementById('revenueChart');

    new Chart(ctx, {

      type: 'line',

      data: {

        labels: labels,

        datasets: [{

          label: 'Revenue',

          data: data,

          borderColor: '#2563eb',

          backgroundColor: 'rgba(37,99,235,0.2)',

          tension: 0.4,

          fill: true

        }]

      },

      options: {

        responsive: true,

        plugins: {

          legend: {
            display: false
          }

        },

        scales: {

          y: {
            beginAtZero: true
          }

        }

      }

    });

  });

</script>

</body>
</html>

