<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title>Properties</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer/property.css">
</head>

<body>

<div class="section">

    <div class="container">

        <h2 class="mb-4">Find Your Dream Property</h2>

        <form method="get"
              action="${pageContext.request.contextPath}/customer/properties">

            <input type="text"
                   name="address"
                   placeholder="Address"
                   value="${param.address}">

            <select name="type">
                <option value="">All</option>
                <option value="SALE">Sale</option>
                <option value="RENT">Rent</option>
            </select>

            <input type="number"
                   name="minPrice"
                   placeholder="Min Price"
                   value="${param.minPrice}">

            <input type="number"
                   name="maxPrice"
                   placeholder="Max Price"
                   value="${param.maxPrice}">

            <select name="sort">
                <option value="">Newest</option>
                <option value="price_asc">Price Low → High</option>
                <option value="price_desc">Price High → Low</option>
            </select>

            <button>Search</button>

        </form>

        <div class="property-grid">

            <c:forEach var="p" items="${properties}">

                <div class="property-card">

                    <img src="${pageContext.request.contextPath}${p.thumbnail}">

                    <h3>${p.title}</h3>

                    <p>${p.address}</p>

                    <p class="price">${p.price} VND</p>

                    <p class="type">${p.type}</p>

                    <a class="btn-view"
                       href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                        View Detail
                    </a>

                </div>

            </c:forEach>

        </div>

        <div class="pagination">

            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage-1}">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">

                <a href="?page=${i}"
                   class="${i==currentPage?'active':''}">
                        ${i}
                </a>

            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage+1}">Next</a>
            </c:if>

        </div>

    </div>

</div>


</body>
</html>

<%@ include file="../common/footer.jsp" %>