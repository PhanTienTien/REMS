<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/property-detail.css">

<div class="property-container">

  <jsp:include page="property-gallery.jsp"/>

  <jsp:include page="property-overview.jsp"/>

  <jsp:include page="property-location.jsp"/>

  <jsp:include page="property-similar.jsp"/>

  <jsp:include page="property-booking.jsp"/>

</div>

<%@ include file="../common/footer.jsp" %>
