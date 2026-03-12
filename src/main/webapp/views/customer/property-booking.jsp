<div class="booking">

  <c:choose>

    <c:when test="${property.status == 'AVAILABLE'}">

      <a class="btn-book"
         href="${pageContext.request.contextPath}/customer/bookings/create?propertyId=${property.id}">
        Book Now
      </a>

    </c:when>

    <c:otherwise>

      <p class="not-available">
        Property not available
      </p>

    </c:otherwise>

  </c:choose>

</div>