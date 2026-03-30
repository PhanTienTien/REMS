<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

    <title>Booking Management</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/property-dashboard.css">

</head>

<body>

<div class="dashboard-container">

    <!-- SIDEBAR -->
    <c:choose>
        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
            <jsp:include page="/views/admin/components/sidebar.jsp"/>
        </c:when>
        <c:otherwise>
            <jsp:include page="/views/staff/components/sidebar.jsp"/>
        </c:otherwise>
    </c:choose>

    <div class="main-content">

        <!-- TOPBAR -->
        <c:choose>
            <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                <jsp:include page="/views/admin/components/topbar.jsp"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="/views/staff/components/topbar.jsp"/>
            </c:otherwise>
        </c:choose>

        <div class="content">

            <h2>Booking Management</h2>

            <!-- 🔍 SEARCH + FILTER + SORT -->
            <form method="get"
                  action="${pageContext.request.contextPath}/admin/bookings"
                  class="search-bar">

                <input type="text"
                       name="keyword"
                       value="${keyword}"
                       placeholder="Search property or customer">

                <select name="status">
                    <option value="">All Status</option>
                    <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="ACCEPTED" ${status == 'ACCEPTED' ? 'selected' : ''}>Accepted</option>
                    <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                    <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                </select>

                <select name="sort">
                    <option value="">Newest</option>
                    <option value="oldest" ${sort == 'oldest' ? 'selected' : ''}>Oldest</option>
                </select>

                <button class="btn-search">Search</button>

            </form>

            <!-- TABLE -->
            <div class="table-wrapper">

                <table class="property-table">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Property</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach items="${result.data}" var="b">

                        <tr>

                            <td>${b.bookingId}</td>
                            <td>${b.propertyTitle}</td>
                            <td>${b.customerName}</td>

                            <td>
                                <span class="status-badge status-${b.status}">
                                        ${b.status}
                                </span>
                            </td>

                            <td>${b.createdAt}</td>

                            <td class="actions">

                                <a class="btn-view"
                                   href="${pageContext.request.contextPath}/admin/bookings?action=view&id=${b.bookingId}">
                                    View
                                </a>

                                <c:if test="${b.status == 'PENDING'}">

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">

                                        <input type="hidden" name="action" value="accept"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>

                                        <button class="btn-approve">
                                            Accept
                                        </button>
                                    </form>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">

                                        <input type="hidden" name="action" value="reject"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>

                                        <button class="btn-delete">
                                            Reject
                                        </button>
                                    </form>

                                </c:if>

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

            <!-- PAGINATION -->
            <div class="pagination">

                <c:if test="${result.page > 1}">
                    <a href="${baseUrl}&page=${result.page - 1}">
                        Previous
                    </a>
                </c:if>

                <c:forEach begin="1" end="${result.totalPages}" var="i">
                    <a href="${baseUrl}&page=${i}"
                       class="${i == result.page ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${result.page < result.totalPages}">
                    <a href="${baseUrl}&page=${result.page + 1}">
                        Next
                    </a>
                </c:if>

            </div>

        </div>

    </div>

</div>

</body>
</html>