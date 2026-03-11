<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

  <title>Activity Logs - REMS Admin</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/admin/activitylog.css">

</head>

<body>

<div class="dashboard-container">

  <jsp:include page="components/sidebar.jsp"/>

  <div class="main-content">

    <jsp:include page="components/topbar.jsp"/>

    <div class="dashboard-content">

      <div class="transactions-container">

        <h3>Activity Logs</h3>

        <!-- FILTER -->

        <div class="filter-container">

          <form method="get"
                action="${pageContext.request.contextPath}/admin/activity-logs">

            <input type="text"
                   name="user"
                   placeholder="Search user"
                   value="${param.user}">

            <select name="action">

              <option value="">All Actions</option>

              <option value="CREATE_PROPERTY">Create Property</option>
              <option value="UPDATE_PROPERTY">Update Property</option>
              <option value="DELETE_PROPERTY">Delete Property</option>

              <option value="CREATE_BOOKING">Create Booking</option>
              <option value="CANCEL_BOOKING">Cancel Booking</option>

              <option value="CREATE_TRANSACTION">Create Transaction</option>
              <option value="COMPLETE_TRANSACTION">Complete Transaction</option>

            </select>

            <input type="date" name="fromDate" value="${param.fromDate}">
            <input type="date" name="toDate" value="${param.toDate}">

            <button type="submit">Filter</button>

          </form>

        </div>

        <!-- TABLE -->

        <table>

          <thead>
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Action</th>
            <th>Entity</th>
            <th>Description</th>
            <th>Date</th>
          </tr>
          </thead>

          <tbody>

          <c:forEach items="${logs}" var="log">

            <tr>

              <td>${log.id}</td>

              <td>${log.fullName}</td>

              <td>

                <span class="badge
                <c:choose>

                  <c:when test="${log.action == 'CREATE_PROPERTY'}">
                    badge-completed
                  </c:when>

                  <c:when test="${log.action == 'DELETE_PROPERTY'}">
                    badge-failed
                  </c:when>

                  <c:otherwise>
                    badge-pending
                  </c:otherwise>

                </c:choose>
                ">

                    ${log.action}

                </span>

              </td>

              <td>${log.entityType} #${log.entityId}</td>

              <td>${log.description}</td>

              <td>${log.createdAt}</td>

            </tr>

          </c:forEach>

          </tbody>

        </table>

      </div>

      <!-- PAGINATION -->

      <div class="pagination">

        <c:if test="${currentPage > 1}">
          <a href="?page=${currentPage-1}">Previous</a>
        </c:if>

        <span>Page ${currentPage}</span>

        <a href="?page=${currentPage+1}">Next</a>

      </div>

    </div>

  </div>

</div>

</body>
</html>