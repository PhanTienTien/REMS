<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/bookings.css">

<div class="profile-page">
    <div class="profile-wrapper">
        <jsp:include page="components/profile-sidebar.jsp"/>

        <div class="profile-content">
            <div class="profile-card">
                <div class="list-shell">
                    <div class="list-toolbar">
                        <div>
                            <h2>Bất động sản đã đặt</h2>
                            <p class="section-subtitle">
                                Theo dõi toàn bộ yêu cầu đặt lịch xem bất động sản và trạng thái xử lý của từng lịch hẹn.
                            </p>
                        </div>
                        <div class="list-meta">Tổng số lịch hẹn: ${pagination.totalItems}</div>
                    </div>

                    <c:choose>
                        <c:when test="${empty bookings}">
                            <div class="empty-state">Bạn chưa có lịch hẹn nào.</div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-wrap">
                                <table class="booking-table">
                                    <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Bất động sản</th>
                                        <th>Trạng thái</th>
                                        <th>Thời gian hẹn xem</th>
                                        <th>Thời gian tạo</th>
                                        <th>Ghi chú</th>
                                        <th>Thao tác</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${bookings}" var="b" varStatus="loop">
                                        <tr>
                                            <td data-label="STT">${loop.index + 1}</td>
                                            <td data-label="Bất động sản">
                                                <div class="table-primary">${b.propertyTitle}</div>
                                            </td>
                                            <td data-label="Trạng thái">
                                                <span class="status-pill status ${b.status}">
                                                    <c:choose>
                                                        <c:when test="${b.status == 'PENDING'}">Chờ xử lý</c:when>
                                                        <c:when test="${b.status == 'ACCEPTED'}">Đã chấp nhận</c:when>
                                                        <c:when test="${b.status == 'REJECTED'}">Đã từ chối</c:when>
                                                        <c:when test="${b.status == 'COMPLETED'}">Hoàn tất</c:when>
                                                        <c:when test="${b.status == 'CANCELLED'}">Đã hủy</c:when>
                                                        <c:otherwise>${b.status}</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td data-label="Thời gian hẹn xem">
                                                <div class="table-primary">
                                                    <c:choose>
                                                        <c:when test="${not empty b.scheduledAt}">${b.scheduledAt}</c:when>
                                                        <c:otherwise>Chưa chọn</c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td data-label="Thời gian tạo">
                                                <div class="table-secondary">${b.createdAt}</div>
                                            </td>
                                            <td data-label="Ghi chú">
                                                <div class="table-secondary">
                                                    <c:choose>
                                                        <c:when test="${not empty b.note}">${b.note}</c:when>
                                                        <c:otherwise>Không có ghi chú</c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td data-label="Thao tác">
                                                <c:if test="${b.status == 'PENDING'}">
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/customer/profile/bookings">
                                                        <input type="hidden" name="action" value="cancel"/>
                                                        <input type="hidden" name="id" value="${b.bookingId}"/>
                                                        <button type="submit" class="btn-cancel">Hủy lịch</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${b.status != 'PENDING'}">-</c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    
                    <%-- Include Pagination Component --%>
                    <jsp:include page="../../common/components/pagination.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %>
