<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

  <title>Transactions - REMS Admin</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/admin/transaction-dashboard.css">

</head>

<body>

<div class="dashboard-container">

  <jsp:include page="components/sidebar.jsp"/>

  <div class="main-content">

    <jsp:include page="components/topbar.jsp"/>

    <div class="dashboard-content">

      <div class="transactions-container">

        <div class="page-header">

          <h2>Transactions</h2>

          <div class="filter-box">

            <a class="filter-btn"
               href="${pageContext.request.contextPath}/admin/transactions">
              All
            </a>

            <a class="filter-btn"
               href="${pageContext.request.contextPath}/admin/transactions?status=PENDING">
              Pending
            </a>

            <a class="filter-btn"
               href="${pageContext.request.contextPath}/admin/transactions?status=COMPLETED">
              Completed
            </a>

            <a class="filter-btn"
               href="${pageContext.request.contextPath}/admin/transactions?status=FAILED">
              Failed
            </a>

          </div>

        </div>

        <table>

          <thead>
          <tr>
            <th>ID</th>
            <th>Property</th>
            <th>Customer</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Type</th>
            <th>Date</th>
            <th>Processed By</th>
            <th>Action</th>
          </tr>
          </thead>

          <tbody>

          <c:forEach items="${transactions}" var="tx">

            <tr>

              <td>${tx.id}</td>

              <td>${tx.propertyTitleSnapshot}</td>

              <td>${tx.customerNameSnapshot}</td>

              <td class="money">${tx.amount}</td>

              <td>

                <span class="badge
                <c:choose>
                    <c:when test="${tx.status == 'COMPLETED'}">badge-completed</c:when>
                    <c:when test="${tx.status == 'PENDING'}">badge-pending</c:when>
                    <c:when test="${tx.status == 'FAILED'}">badge-failed</c:when>
                    <c:otherwise>badge-cancelled</c:otherwise>
                </c:choose>
                ">
                    ${tx.status}
                </span>

              </td>

              <td>${tx.type}</td>

              <td>${tx.createdAt}</td>

              <td>${tx.processedBy}</td>

              <td>

                <a class="action-btn view"
                   href="${pageContext.request.contextPath}/admin/transactions/view?id=${tx.id}">
                  View
                </a>

                <c:if test="${tx.status == 'PENDING'}">

                  <form method="post"
                        action="${pageContext.request.contextPath}/admin/transactions/complete"
                        class="complete-form">

                    <input type="hidden"
                           name="bookingId"
                           value="${tx.bookingId}"/>

                    <button class="action-btn complete">
                      Complete
                    </button>

                  </form>

                </c:if>

              </td>

            </tr>

          </c:forEach>

          </tbody>

        </table>

      </div>

    </div>

  </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin/transaction-dashboard.js"></script>

</body>
</html>