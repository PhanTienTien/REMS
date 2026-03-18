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
    <div class="property-container">

        <div class="gallery">

            <c:forEach var="img" items="${images}">
                <img class="gallery-img"
                     src="${pageContext.request.contextPath}${img.imageUrl}"
                     alt="Property Image">
            </c:forEach>

        </div>

        <div class="overview">

            <h1>${property.title}</h1>

            <p class="price">

                $${property.price}

            </p>

            <p class="address">

                ${property.address}

            </p>

            <p>

                Type: ${property.type}

            </p>

            <p>

                ${property.description}

            </p>

        </div>

        <div class="location">

            <h2>Location</h2>

            <p>

                Address: ${property.address}

            </p>

        </div>

        <div class="similar">

            <h2>Similar Properties</h2>

            <div class="similar-grid">

                <c:forEach var="p" items="${similar}">

                    <div class="card">

                        <img src="${pageContext.request.contextPath}${thumbnails[p.id]}">

                        <h3>${p.title}</h3>

                        <p class="price">
                            <fmt:formatNumber value="${p.price}" type="number"/> đ
                        </p>

                        <a href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                            View
                        </a>

                    </div>

                </c:forEach>

            </div>

        </div>

        <div class="booking">

            <c:choose>

                <c:when test="${property.status == 'AVAILABLE'}">

                    <form method="post"
                          action="${pageContext.request.contextPath}/customer/bookings/create">

                        <input type="hidden"
                               name="propertyId"
                               value="${property.id}"/>

                        <textarea name="note"
                                  placeholder="Message to agent (optional)"
                                  class="booking-note"></textarea>

                        <c:choose>

                            <c:when test="${not empty sessionScope.currentUser}">

                                <button type="submit"
                                        class="btn-book">
                                    Book Viewing
                                </button>

                            </c:when>

                            <c:otherwise>

                                <button type="button"
                                        class="btn-book"
                                        onclick="requireLogin()">
                                    Book Viewing
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

                            Save

                        </button>

                    </form>

                </c:when>

                <c:otherwise>

                    <p class="not-available">
                        Property not available
                    </p>

                </c:otherwise>

            </c:choose>

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
