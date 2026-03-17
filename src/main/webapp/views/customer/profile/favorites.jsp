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

                <h2>My Favorite Properties</h2>

                <c:if test="${empty favorites}">
                    <p class="empty">
                        You haven't saved any properties yet.
                    </p>
                </c:if>

                <div class="property-grid">

                    <c:forEach var="p" items="${favorites}">

                        <div class="favorite-card">

                            <img src="${pageContext.request.contextPath}${p.thumbnail}"
                                 alt="${p.title}">

                            <h3>${p.title}</h3>

                            <p class="price">$${p.price}</p>

                            <a class="btn-view"
                               href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
                                View Detail
                            </a>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

    </div>

</div>

<%@ include file="../../common/footer.jsp" %>