<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="views/common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/layout.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/property.css">

<div class="hero">
    <div class="hero-slide">
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_3.jpg')"></div>
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_2.jpg')"></div>
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_1.jpg')"></div>
    </div>

    <div class="container">
        <div class="row justify-content-center align-items-center">
            <div class="col-lg-9 text-center">
                <h1 class="heading">
                    Tìm bất động sản phù hợp cho nhu cầu ở, đầu tư hoặc cho thuê
                </h1>

                <form action="${pageContext.request.contextPath}/customer/properties"
                      method="get"
                      class="narrow-w form-search d-flex align-items-stretch mb-3">
                    <input type="text"
                           class="form-control px-4"
                           placeholder="Nhập khu vực, địa chỉ hoặc tên bất động sản"
                           name="address" />

                    <button type="submit" class="btn btn-primary">
                        Tìm kiếm
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="page-section">
    <div class="container">
        <div class="section-shell">
            <div class="row mb-4 align-items-center">
                <div class="col-lg-7">
                    <h2 class="section-heading">6 bất động sản được xem nhiều nhất</h2>
                    <p class="section-subtitle">
                        Danh sách này được gợi ý dựa trên lượt xem chi tiết của khách hàng trong hệ thống.
                    </p>
                </div>
                <div class="col-lg-5 text-lg-end">
                    <a href="${pageContext.request.contextPath}/customer/properties"
                       class="btn-view">
                        Xem toàn bộ bất động sản
                    </a>
                </div>
            </div>

            <div class="property-grid">
                <c:forEach var="p" items="${properties}">
                    <div class="property-card">
                        <img src="${pageContext.request.contextPath}${p.thumbnail}"
                             alt="${p.title}">

                        <div class="card-body">
                            <span class="badge">
                                <c:choose>
                                    <c:when test="${p.type == 'SALE'}">Bất động sản bán</c:when>
                                    <c:otherwise>Bất động sản cho thuê</c:otherwise>
                                </c:choose>
                            </span>

                            <h3>${p.title}</h3>
                            <p class="address">${p.address}</p>
                            <p class="price">
                                <fmt:formatNumber value="${p.price}" type="number"/> VNĐ
                            </p>

                            <a href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}"
                               class="btn-view">
                                Xem chi tiết
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${empty properties}">
                <div class="empty-state mt-4">
                    Chưa có đủ dữ liệu lượt xem để gợi ý bất động sản trên trang chủ.
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="views/common/footer.jsp" %>
