<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Giao dịch - REMS Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/transaction-dashboard.css">
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
                <div class="page-header">
                    <h2>Giao dịch</h2>

                    <form method="get"
                          action="${pageContext.request.contextPath}/admin/transactions">
                        <input type="text"
                               name="keyword"
                               placeholder="Tìm bất động sản, khách hàng..."
                               value="${keyword}"/>

                        <select name="status">
                            <option value="">Tất cả trạng thái</option>
                            <option value="PENDING" ${status=='PENDING'?'selected':''}>Chờ xử lý</option>
                            <option value="COMPLETED" ${status=='COMPLETED'?'selected':''}>Hoàn tất</option>
                            <option value="CANCELLED" ${status=='CANCELLED'?'selected':''}>Đã hủy</option>
                        </select>

                        <button type="submit">Tìm kiếm</button>
                    </form>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>
                            <a href="?sortBy=property&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Bất động sản
                            </a>
                        </th>
                        <th>Khách hàng</th>
                        <th>
                            <a href="?sortBy=amount&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Số tiền
                            </a>
                        </th>
                        <th>Trạng thái</th>
                        <th>Loại</th>
                        <th>
                            <a href="?sortBy=created_at&sortDir=${sortDir=='asc'?'desc':'asc'}">
                                Ngày
                            </a>
                        </th>
                        <th>Người xử lý</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${transactions}" var="tx" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.index + 1}</td>
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
                            <td>${not empty tx.processedByName ? tx.processedByName : '-'}</td>
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
                                        data-processed="${not empty tx.processedByName ? tx.processedByName : '-'}">
                                    Xem
                                </button>

                                <c:if test="${tx.status == 'PENDING'}">
                                    <form method="post"
                                          class="complete-form"
                                          action="${pageContext.request.contextPath}/admin/transactions"
                                          style="display:inline">
                                        <input type="hidden" name="action" value="complete"/>
                                        <input type="hidden" name="transactionId" value="${tx.id}"/>
                                        <button type="submit">Hoàn tất</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div id="transactionModal" class="modal">
                    <div class="modal-content">
                        <span class="close-btn">&times;</span>

                        <h3>Chi tiết giao dịch</h3>

                        <p><strong>ID:</strong> <span id="m-id"></span></p>
                        <p><strong>Bất động sản:</strong> <span id="m-property"></span></p>
                        <p><strong>Khách hàng:</strong> <span id="m-customer"></span></p>
                        <p><strong>Số tiền:</strong> <span id="m-amount"></span></p>
                        <p><strong>Trạng thái:</strong> <span id="m-status"></span></p>
                        <p><strong>Loại:</strong> <span id="m-type"></span></p>
                        <p><strong>Ngày:</strong> <span id="m-date"></span></p>
                        <p><strong>Người xử lý:</strong> <span id="m-processed"></span></p>
                    </div>
                </div>

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${baseUrl}&page=${currentPage - 1}">Trước</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="${baseUrl}&page=${i}"
                           class="${i==currentPage?'active':''}">
                                ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${baseUrl}&page=${currentPage + 1}">Sau</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin/transaction-dashboard.js?v=2"></script>
</body>
</html>
