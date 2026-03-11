<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

                <h3>Bookings</h3>

                <table>

                    <thead>

                    <tr>

                        <th>ID</th>
                        <th>Property</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Accepted By</th>
                        <th>Action</th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:forEach items="${bookings}" var="b">

                        <tr>

                            <td>${b.id}</td>

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

                                    <c:otherwise>
                                        badge-failed
                                    </c:otherwise>
                                </c:choose>
                                ">

                                        ${b.status}

                                </span>

                            </td>

                            <td>${b.createdAt}</td>

                            <td>
                                <c:if test="${b.acceptedByName != null}">
                                    ${b.acceptedByName}
                                </c:if>
                            </td>

                            <td>

                                <c:if test="${b.status == 'PENDING'}">

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings/accept"
                                          style="display:inline;">

                                        <input type="hidden" name="id" value="${b.id}"/>

                                        <button class="btn btn-accept">
                                            Accept
                                        </button>

                                    </form>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings/reject"
                                          style="display:inline;">

                                        <input type="hidden" name="id" value="${b.id}"/>

                                        <button class="btn btn-reject">
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

        </div>

    </div>

</div>

</body>
</html>