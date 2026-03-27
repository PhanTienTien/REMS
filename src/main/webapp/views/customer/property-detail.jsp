<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/property-detail.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/layout.css">

<div class="page-section">
    <div class="container property-detail">

        <!-- LEFT -->
        <div class="left">

            <!-- GALLERY -->
            <div class="gallery">
                <c:forEach var="img" items="${images}">
                    <img class="gallery-img"
                         src="${pageContext.request.contextPath}${img.imageUrl}">
                </c:forEach>
            </div>

            <!-- OVERVIEW -->
            <div class="overview card">

                <h1>${property.title}</h1>

                <p class="price">
                    <fmt:formatNumber value="${property.price}" type="number"/> đ
                </p>

                <p class="address">${property.address}</p>

                <p class="type">
                    <c:choose>
                        <c:when test="${property.type == 'SALE'}">Bán</c:when>
                        <c:when test="${property.type == 'RENT'}">Cho thuê</c:when>
                        <c:otherwise>${property.type}</c:otherwise>
                    </c:choose>
                </p>

                <p class="desc">${property.description}</p>

            </div>

            <!-- LOCATION -->
            <div class="location card">
                <h2>Vị trí</h2>
                <p>${property.address}</p>
            </div>

            <!-- SIMILAR -->
            <div class="similar">

                <h2>Bất động sản tương tự</h2>

                <div class="similar-grid">

                    <c:forEach var="p" items="${similar}">

                        <div class="card">

                            <img src="${pageContext.request.contextPath}${thumbnails[p.id]}">

                            <div class="card-body">

                                <h3>${p.title}</h3>

                                <p class="price">
                                    <fmt:formatNumber value="${p.price}" type="number"/> đ
                                </p>

                                <a class="btn-view"
                                   href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                                    Xem
                                </a>

                            </div>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

        <!-- RIGHT (BOOKING STICKY) -->
        <div class="right">

            <div class="booking card">

                <c:choose>

                    <c:when test="${property.status == 'AVAILABLE'}">

                        <form method="post"
                              action="${pageContext.request.contextPath}/customer/bookings/create">

                            <input type="hidden"
                                   name="propertyId"
                                   value="${property.id}"/>

                            <textarea name="note"
                                      placeholder="Nhắn cho môi giới (không bắt buộc)"
                                      class="booking-note"></textarea>

                            <c:choose>

                                <c:when test="${not empty sessionScope.currentUser}">

                                    <button type="submit"
                                            class="btn-book">
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

                            <button class="btn-favorite">
                                Lưu yêu thích
                            </button>

                        </form>

                    </c:when>

                    <c:otherwise>

                        <p class="not-available">
                            Bất động sản hiện không khả dụng
                        </p>

                    </c:otherwise>

                </c:choose>

            </div>

        </div>

    </div>
</div>

<script>

    function bookProperty(id){
        window.location.href =
            "${pageContext.request.contextPath}/bookings/create?propertyId=" + id;
    }

    function requireLogin(){
        alert("Please login to book this property");
        window.location.href =
            "${pageContext.request.contextPath}/login";
    }

</script>

<%@ include file="../common/footer.jsp" %>
