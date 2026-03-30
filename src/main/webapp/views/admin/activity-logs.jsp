<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Nhật ký hoạt động - REMS Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/activitylog.css">
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
                <h3>Nhật ký hoạt động</h3>

                <div class="filter-container">
                    <form method="get"
                          action="${pageContext.request.contextPath}/admin/activity-logs">

                        <input type="text"
                               name="user"
                               placeholder="Tìm người dùng"
                               value="${userFilter}">

                        <select name="action">
                            <option value="">Tất cả hành động</option>
                            <option value="CREATE_PROPERTY" ${actionFilter == 'CREATE_PROPERTY' ? 'selected' : ''}>Tạo bất động sản</option>
                            <option value="APPROVE_PROPERTY" ${actionFilter == 'APPROVE_PROPERTY' ? 'selected' : ''}>Duyệt bất động sản</option>
                            <option value="DELETE_PROPERTY" ${actionFilter == 'DELETE_PROPERTY' ? 'selected' : ''}>Xóa bất động sản</option>
                            <option value="DEACTIVATE_PROPERTY" ${actionFilter == 'DEACTIVATE_PROPERTY' ? 'selected' : ''}>Ẩn bất động sản</option>
                            <option value="RESTORE_PROPERTY" ${actionFilter == 'RESTORE_PROPERTY' ? 'selected' : ''}>Khôi phục bất động sản</option>
                            <option value="CREATE_BOOKING" ${actionFilter == 'CREATE_BOOKING' ? 'selected' : ''}>Tạo đặt lịch</option>
                            <option value="ACCEPT_BOOKING" ${actionFilter == 'ACCEPT_BOOKING' ? 'selected' : ''}>Chấp nhận đặt lịch</option>
                            <option value="REJECT_BOOKING" ${actionFilter == 'REJECT_BOOKING' ? 'selected' : ''}>Từ chối đặt lịch</option>
                            <option value="CANCEL_BOOKING" ${actionFilter == 'CANCEL_BOOKING' ? 'selected' : ''}>Hủy đặt lịch</option>
                            <option value="CREATE_TRANSACTION" ${actionFilter == 'CREATE_TRANSACTION' ? 'selected' : ''}>Tạo giao dịch</option>
                            <option value="COMPLETE_TRANSACTION" ${actionFilter == 'COMPLETE_TRANSACTION' ? 'selected' : ''}>Hoàn tất giao dịch</option>
                        </select>

                        <input type="date" name="fromDate" value="${fromDateFilter}">
                        <input type="date" name="toDate" value="${toDateFilter}">

                        <button type="submit">Lọc</button>
                    </form>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Người dùng</th>
                        <th>Hành động</th>
                        <th>Đối tượng</th>
                        <th>Mô tả</th>
                        <th>Ngày</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${logs}" var="log" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${log.fullName}</td>
                            <td>
                                <span class="badge
                                <c:choose>
                                  <c:when test="${log.action == 'CREATE_PROPERTY'}">badge-completed</c:when>
                                  <c:when test="${log.action == 'DELETE_PROPERTY'}">badge-failed</c:when>
                                  <c:otherwise>badge-pending</c:otherwise>
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

            <div class="pagination">
                <c:url var="baseUrl" value="/admin/activity-logs">
                    <c:param name="user" value="${userFilter}" />
                    <c:param name="action" value="${actionFilter}" />
                    <c:param name="fromDate" value="${fromDateFilter}" />
                    <c:param name="toDate" value="${toDateFilter}" />
                </c:url>

                <c:if test="${currentPage > 1}">
                    <a href="${baseUrl}&page=${currentPage - 1}">Trước</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${baseUrl}&page=${i}"
                       class="${i == currentPage ? 'active' : ''}">
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
</body>
</html>
