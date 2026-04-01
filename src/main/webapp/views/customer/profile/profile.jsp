<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">

<div class="profile-page">

    <div class="profile-wrapper">

        <!-- Sidebar -->
        <jsp:include page="components/profile-sidebar.jsp"/>

        <!-- Content -->
        <jsp:include page="components/profile-card.jsp"/>

    </div>

</div>

<%@ include file="../../common/footer.jsp" %>