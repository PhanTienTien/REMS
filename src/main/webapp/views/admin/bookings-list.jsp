<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="csrf" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Quản lý đặt lịch</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/property-dashboard.css">
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

        <div class="content">
            <h2>Quản lý đặt lịch</h2>

            <form method="get"
                  action="${pageContext.request.contextPath}/admin/bookings"
                  class="search-bar">
                <input type="text"
                       name="keyword"
                       value="${keyword}"
                       placeholder="Tìm theo bất động sản hoặc khách hàng">

                <select name="status">
                    <option value="">Tất cả trạng thái</option>
                    <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                    <option value="ACCEPTED" ${status == 'ACCEPTED' ? 'selected' : ''}>Đã chấp nhận</option>
                    <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>Đã từ chối</option>
                    <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                </select>

                <select name="sort">
                    <option value="">Mới nhất</option>
                    <option value="oldest" ${sort == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                </select>

                <button class="btn-search">Tìm kiếm</button>
            </form>

            <div class="table-wrapper">
                <table class="property-table">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Bất động sản</th>
                        <th>Khách hàng</th>
                        <th>Trạng thái</th>
                        <th>Thời gian hẹn</th>
                        <th>Ngày tạo</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.data}" var="b" varStatus="loop">
                        <tr>
                            <td>${(result.page - 1) * result.size + loop.index + 1}</td>
                            <td>${b.propertyTitle}</td>
                            <td>${b.customerName}</td>
                            <td>
                                <span class="status-badge status-${b.status}">
                                    ${b.status}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty b.scheduledAt}">${b.scheduledAt}</c:when>
                                    <c:otherwise>Chưa chọn</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${b.createdAt}</td>
                            <td class="actions">
                                <a class="btn-view"
                                   href="${pageContext.request.contextPath}/admin/bookings?action=view&id=${b.bookingId}">
                                    Xem
                                </a>

                                <c:if test="${b.status == 'PENDING'}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">
                                        <csrf:csrfToken/>
                                        <input type="hidden" name="action" value="accept"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>
                                        <button class="btn-approve">Chấp nhận</button>
                                    </form>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/bookings">
                                        <csrf:csrfToken/>
                                        <input type="hidden" name="action" value="reject"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>
                                        <button class="btn-delete">Từ chối</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <c:if test="${result.page > 1}">
                    <a href="${baseUrl}&page=${result.page - 1}">Trước</a>
                </c:if>

                <c:forEach begin="1" end="${result.totalPages}" var="i">
                    <a href="${baseUrl}&page=${i}"
                       class="${i == result.page ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${result.page < result.totalPages}">
                    <a href="${baseUrl}&page=${result.page + 1}">Sau</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
