<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/transactions.css">

<div class="profile-page">
    <div class="profile-wrapper">
        <jsp:include page="components/profile-sidebar.jsp"/>

        <div class="profile-content">
            <div class="profile-card">
                <h2>Giao dịch của bạn</h2>
                <p class="section-subtitle">
                    Theo dõi lịch sử giao dịch phát sinh từ các lịch hẹn đã được xử lý.
                </p>

                <c:choose>
                    <c:when test="${empty transactions}">
                        <div class="empty-state">Bạn chưa có giao dịch nào.</div>
                    </c:when>
                    <c:otherwise>
                        <table class="transaction-table">
                            <thead>
                            <tr>
                                <th>STT</th>
                                <th>Bất động sản</th>
                                <th>Số tiền</th>
                                <th>Loại giao dịch</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="tx" items="${transactions}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${tx.propertyTitleSnapshot}</td>
                                    <td class="money">
                                        <fmt:formatNumber value="${tx.amount}" type="number"/> VNĐ
                                    </td>
                                    <td>${tx.type}</td>
                                    <td>
                                        <span class="status-pill
                                        <c:choose>
                                            <c:when test='${tx.status == "COMPLETED"}'>badge-completed</c:when>
                                            <c:when test='${tx.status == "PENDING"}'>badge-pending</c:when>
                                            <c:otherwise>badge-failed</c:otherwise>
                                        </c:choose>">
                                            ${tx.status}
                                        </span>
                                    </td>
                                    <td>${tx.createdAt}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %>
