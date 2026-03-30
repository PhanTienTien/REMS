<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

        <div class="dashboard-content">
            <div class="transactions-container">

                <!-- HEADER -->
                <div class="page-header">
                    <h2>Transactions</h2>

                    <!-- 🔍 SEARCH -->
                    <form method="get"
                          action="${pageContext.request.contextPath}/admin/transactions">

                        <input type="text"
                               name="keyword"
                               placeholder="Search property, customer..."
                               value="${keyword}"/>

                        <select name="status">
                            <option value="">All Status</option>
                            <option value="PENDING" ${status=='PENDING'?'selected':''}>Pending</option>
                            <option value="COMPLETED" ${status=='COMPLETED'?'selected':''}>Completed</option>
                            <option value="CANCELLED" ${status=='CANCELLED'?'selected':''}>Cancelled</option>
                        </select>

                        <button type="submit">Search</button>

                    </form>
                </div>

                <!-- TABLE -->
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>

                        <!-- SORT -->
                        <th>
                            <a href="?sortBy=property&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Property
                            </a>
                        </th>

                        <th>Customer</th>

                        <th>
                            <a href="?sortBy=amount&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Amount
                            </a>
                        </th>

                        <th>Status</th>
                        <th>Type</th>

                        <th>
                            <a href="?sortBy=created_at&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Date
                            </a>
                        </th>

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

                            <td>${tx.amount}</td>

                            <td>
<span class="badge
<c:choose>
    <c:when test="${tx.status == 'COMPLETED'}">badge-completed</c:when>
    <c:when test="${tx.status == 'PENDING'}">badge-pending</c:when>
    <c:when test="${tx.status == 'CANCELLED'}">badge-failed</c:when>
</c:choose>
">
        ${tx.status}
</span>
                            </td>

                            <td>${tx.type}</td>

                            <td>${tx.createdAtFormatted}</td>

                            <td>${tx.processedBy}</td>

                            <td>

                                <button type="button"
                                        class="view-btn"
                                        data-id="${tx.id}"
                                        data-property="${tx.propertyTitleSnapshot}"
                                        data-customer="${tx.customerNameSnapshot}"
                                        data-amount="${tx.amount}"
                                        data-status="${tx.status}"
                                        data-type="${tx.type}"
                                        data-date="${tx.createdAtFormatted}"
                                        data-processed="${tx.processedBy}">
                                    View
                                </button>

                                <c:if test="${tx.status == 'PENDING'}">

                                    <form method="post"
                                          class="complete-form"
                                          action="${pageContext.request.contextPath}/admin/transactions"
                                          style="display:inline">

                                        <input type="hidden" name="action" value="complete"/>
                                        <input type="hidden" name="transactionId" value="${tx.id}"/>

                                        <button type="submit">Complete</button>

                                    </form>

                                </c:if>

                            </td>

                        </tr>
                    </c:forEach>

                    </tbody>
                </table>

                <!-- MODAL -->
                <div id="transactionModal" class="modal">

                    <div class="modal-content">

                        <span class="close-btn">&times;</span>

                        <h3>Transaction Detail</h3>

<%--                        <!-- 🖼 IMAGE -->--%>
<%--                        <img id="m-image" src="" style="width:100%; border-radius:10px; margin-bottom:10px;" />--%>

                        <p><strong>ID:</strong> <span id="m-id"></span></p>
                        <p><strong>Property:</strong> <span id="m-property"></span></p>
                        <p><strong>Customer:</strong> <span id="m-customer"></span></p>
                        <p><strong>Amount:</strong> <span id="m-amount"></span></p>
                        <p><strong>Status:</strong> <span id="m-status"></span></p>
                        <p><strong>Type:</strong> <span id="m-type"></span></p>
                        <p><strong>Date:</strong> <span id="m-date"></span></p>
                        <p><strong>Processed By:</strong> <span id="m-processed"></span></p>

                    </div>

                </div>

                </div>

                <!-- 📄 PAGINATION -->
                <div class="pagination">

                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage-1}&keyword=${keyword}&status=${status}">
                            Prev
                        </a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">

                        <a href="?page=${i}&keyword=${keyword}&status=${status}"
                           class="${i==currentPage?'active':''}">
                                ${i}
                        </a>

                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage+1}&keyword=${keyword}&status=${status}">
                            Next
                        </a>
                    </c:if>

                </div>

            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin/transaction-dashboard.js?v=2"></script>

</body>
</html>