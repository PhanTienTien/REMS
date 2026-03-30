<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/layout.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/property.css">

<div class="page-section">
    <div class="container">
        <h2 class="page-title">Khám phá bất động sản phù hợp với bạn</h2>
        <p class="page-lead">
            Lọc nhanh theo khu vực, loại hình và khoảng giá để tìm đúng bất động sản bạn đang quan tâm.
        </p>

        <form method="get"
              action="${pageContext.request.contextPath}/customer/properties"
              class="search-form">

            <input type="text"
                   name="address"
                   placeholder="Khu vực hoặc địa chỉ"
                   value="${param.address}">

            <select name="type">
                <option value="">Tất cả loại hình</option>
                <option value="SALE" ${param.type == 'SALE' ? 'selected' : ''}>Mua bán</option>
                <option value="RENT" ${param.type == 'RENT' ? 'selected' : ''}>Cho thuê</option>
            </select>

            <input type="number"
                   name="minPrice"
                   placeholder="Giá từ"
                   value="${param.minPrice}">

            <input type="number"
                   name="maxPrice"
                   placeholder="Giá đến"
                   value="${param.maxPrice}">

            <select name="sort">
                <option value="">Mới nhất</option>
                <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá cao đến thấp</option>
            </select>

            <button class="btn-search" type="submit">Tìm kiếm</button>
        </form>

        <div id="loading-skeleton" class="property-grid">
            <div class="skeleton-card"></div>
            <div class="skeleton-card"></div>
            <div class="skeleton-card"></div>
        </div>

        <div id="real-data" class="property-grid" style="display:none;">
            <c:forEach var="p" items="${properties}">
                <div class="property-card">
                    <img src="${pageContext.request.contextPath}${p.thumbnail}"
                         alt="${p.title}">

                    <div class="card-body">
                        <span class="badge">
                            <c:choose>
                                <c:when test="${p.type == 'SALE'}">Mua bán</c:when>
                                <c:when test="${p.type == 'RENT'}">Cho thuê</c:when>
                                <c:otherwise>${p.type}</c:otherwise>
                            </c:choose>
                        </span>

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
                </div>
            </c:forEach>

            <c:if test="${empty properties}">
                <div class="empty-state">
                    Không tìm thấy bất động sản phù hợp với bộ lọc hiện tại.
                </div>
            </c:if>
        </div>

        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage-1}&address=${param.address}&type=${param.type}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&sort=${param.sort}">
                    Trước
                </a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="?page=${i}&address=${param.address}&type=${param.type}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&sort=${param.sort}"
                   class="${i==currentPage?'active':''}">
                    ${i}
                </a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage+1}&address=${param.address}&type=${param.type}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&sort=${param.sort}">
                    Sau
                </a>
            </c:if>
        </div>
    </div>
</div>

<script>
    window.addEventListener("load", function () {
        document.getElementById("loading-skeleton").style.display = "none";
        document.getElementById("real-data").style.display = "grid";
    });
</script>

<%@ include file="../common/footer.jsp" %>
