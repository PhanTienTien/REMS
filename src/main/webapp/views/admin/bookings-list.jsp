<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>

    <title>Bookings - REMS Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

</head>

<body>

<div class="dashboard-container">

    <c:choose>

        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
            <jsp:include page="/views/admin/components/sidebar.jsp"/>
        </c:when>

        <c:otherwise>
            <jsp:include page="/views/staff/components/sidebar.jsp"/>
        </c:otherwise>

    </c:choose>

    <div class="main-content">

        <c:choose>

            <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                <jsp:include page="/views/admin/components/topbar.jsp"/>
            </c:when>

            <c:otherwise>
                <jsp:include page="/views/staff/components/topbar.jsp"/>
            </c:otherwise>

        </c:choose>

        <div class="dashboard-content">

            <div class="transactions-container">

                <h2>Booking Management</h2>

                <div class="filters">

                    <a href="${pageContext.request.contextPath}/admin/bookings">
                        All
                    </a>

                    <a href="${pageContext.request.contextPath}/admin/bookings?status=PENDING">
                        Pending
                    </a>

                    <a href="${pageContext.request.contextPath}/admin/bookings?status=ACCEPTED">
                        Accepted
                    </a>

                    <a href="${pageContext.request.contextPath}/admin/bookings?status=REJECTED">
                        Rejected
                    </a>

                    <a href="${pageContext.request.contextPath}/admin/bookings?status=CANCELLED">
                        Cancelled
                    </a>

                </div>

                <table>

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Property</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Action</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach items="${bookings}" var="b">

                        <tr>

                            <td>${b.bookingId}</td>

                            <td>${b.propertyTitle}</td>

                            <td>${b.customerName}</td>

                            <td>

<span class="badge
<c:choose>

<c:when test="${b.status == 'ACCEPTED'}">
badge-completed
</c:when>

<c:when test="${b.status == 'PENDING'}">
badge-pending
</c:when>

<c:when test="${b.status == 'REJECTED'}">
badge-failed
</c:when>

<c:when test="${b.status == 'CANCELLED'}">
badge-cancelled
</c:when>

</c:choose>
">

        ${b.status}

</span>

                            </td>

                            <td>${b.createdAtFormatted}</td>

                            <td class="actions">

                                <a href="${pageContext.request.contextPath}/admin/bookings?action=view&id=${b.bookingId}">
                                    View
                                </a>


                                <c:if test="${b.status == 'PENDING'}">

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">

                                        <input type="hidden" name="action" value="accept"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>

                                        <button class="btn-accept">
                                            Accept
                                        </button>

                                    </form>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">

                                        <input type="hidden" name="action" value="reject"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>

                                        <button class="btn-reject">
                                            Reject
                                        </button>

                                    </form>

                                </c:if>

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

                <div class="pagination">

                    <c:if test="${currentPage > 1}">

                        <a href="?page=${currentPage - 1}">
                            Previous
                        </a>

                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="p">

                        <a href="?page=${p}"
                           class="${p == currentPage ? 'active' : ''}">
                                ${p}
                        </a>

                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">

                        <a href="?page=${currentPage + 1}">
                            Next
                        </a>

                    </c:if>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>