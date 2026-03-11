<div class="sidebar">

  <div class="sidebar-logo">
    <h2>REMS</h2>
  </div>

  <ul class="sidebar-menu">

    <li>
      <a href="${pageContext.request.contextPath}/admin/dashboard">
        Dashboard
      </a>
    </li>

    <li>
      <a href="${pageContext.request.contextPath}/admin/properties">
        Properties
      </a>
    </li>

    <li>
      <a href="${pageContext.request.contextPath}/admin/bookings">
        Bookings
      </a>
    </li>

    <li>
      <a href="${pageContext.request.contextPath}/admin/transactions">
        Transactions
      </a>
    </li>

    <!-- ADMIN ONLY -->
    <c:if test="${sessionScope.role == 'ADMIN'}">

      <li>
        <a href="${pageContext.request.contextPath}/admin/users">
          Users
        </a>
      </li>

      <li>
        <a href="${pageContext.request.contextPath}/admin/reports">
          Reports
        </a>
      </li>

    </c:if>

    <!-- ADMIN + STAFF -->
    <c:if test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'STAFF'}">

      <li>
        <a href="${pageContext.request.contextPath}/admin/activity-logs">
          Activity Logs
        </a>
      </li>

    </c:if>

  </ul>

</div>