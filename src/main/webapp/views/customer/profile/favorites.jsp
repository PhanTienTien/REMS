<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/favorites.css">

<div class="profile-page">
    <div class="profile-wrapper">
        <jsp:include page="components/profile-sidebar.jsp"/>

        <div class="profile-content">
            <div class="profile-card">
                <h2>Bất động sản yêu thích</h2>
                <p class="section-subtitle">
                    Danh sách các bất động sản bạn đã lưu để tiện theo dõi và xem lại sau.
                </p>

                <c:if test="${empty favorites}">
                    <div class="empty-state">
                        Bạn chưa lưu bất động sản nào vào danh sách yêu thích.
                    </div>
                </c:if>

                <div class="property-grid">
                    <c:forEach var="p" items="${favorites}">
                        <div class="favorite-card">
                            <img src="${pageContext.request.contextPath}${p.thumbnail}"
                                 alt="${p.title}">

                            <h3>${p.title}</h3>
                            <p class="address">${p.address}</p>
                            <p class="price">
                                <fmt:formatNumber value="${p.price}" type="number"/> VNĐ
                            </p>

                            <a class="btn-view"
                               href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                                Xem chi tiết
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %>
