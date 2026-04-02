<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/layout.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/property-detail.css">

<div class="page-section">
    <div class="container property-detail">
        <div class="left">
            <div class="card">
                <div class="gallery">
                    <c:forEach var="img" items="${images}">
                        <img class="gallery-img"
                             src="${pageContext.request.contextPath}${img.imageUrl}"
                             alt="${property.title}">
                    </c:forEach>
                </div>
            </div>

            <div class="overview card">
                <h1>${property.title}</h1>

                <p class="price">
                    <fmt:formatNumber value="${property.price}" type="number"/> VNĐ
                </p>

                <p class="address">${property.address}</p>

                <p class="type">
                    <c:choose>
                        <c:when test="${property.type == 'SALE'}">Mua bán</c:when>
                        <c:when test="${property.type == 'RENT'}">Cho thuê</c:when>
                        <c:otherwise>${property.type}</c:otherwise>
                    </c:choose>
                </p>

                <p class="desc">${property.description}</p>
            </div>

            <div class="location card">
                <h2 class="section-heading">Vị trí</h2>
                <p class="address">${property.address}</p>
            </div>

            <div class="similar card">
                <h2 class="section-heading">Bất động sản tương tự</h2>

                <div class="similar-grid">
                    <c:forEach var="p" items="${similar}">
                        <div class="card">
                            <img src="${pageContext.request.contextPath}${thumbnails[p.id]}"
                                 alt="${p.title}">

                            <div class="card-body">
                                <h3>${p.title}</h3>
                                <p class="price">
                                    <fmt:formatNumber value="${p.price}" type="number"/> VNĐ
                                </p>

                                <a class="btn-view"
                                   href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                                    Xem chi tiết
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="right">
            <div class="booking card">
                <h2 class="section-heading">Đăng ký quan tâm</h2>
                <p class="section-subtitle">
                    Gửi yêu cầu để được nhân viên liên hệ và hỗ trợ tư vấn chi tiết hơn.
                </p>

                <c:choose>
                    <c:when test="${property.status == 'AVAILABLE'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/customer/bookings/create">

                            <input type="hidden"
                                   name="propertyId"
                                   value="${property.id}"/>

                            <textarea name="note"
                                      placeholder="Nhắn cho nhân viên tư vấn nếu bạn có yêu cầu thêm"
                                      class="booking-note"></textarea>

                            <c:choose>
                                <c:when test="${not empty sessionScope.currentUser}">
                                    <button type="submit" class="btn-book">
                                        Đặt lịch xem
                                    </button>
                                </c:when>

                                <c:otherwise>
                                    <button type="button"
                                            class="btn-book"
                                            onclick="requireLogin()">
                                        Đặt lịch xem
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>

                        <form method="post"
                              action="${pageContext.request.contextPath}/customer/favorites/add">

                            <input type="hidden"
                                   name="propertyId"
                                   value="${property.id}"/>

                            <button class="btn-favorite" type="submit">
                                Lưu vào danh sách yêu thích
                            </button>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <p class="not-available">
                            Bất động sản này hiện chưa sẵn sàng để đặt lịch xem.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script>
    function requireLogin() {
        alert("Vui lòng đăng nhập để đặt lịch xem bất động sản này.");
        window.location.href = "${pageContext.request.contextPath}/auth";
    }
</script>

<%@ include file="../common/footer.jsp" %>
