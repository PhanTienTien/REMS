<div class="similar">

  <h2>Similar Properties</h2>

  <div class="similar-grid">

    <c:forEach var="p" items="${similar}">

      <div class="card">

        <img src="${pageContext.request.contextPath}/assets/images/property.jpg">

        <h3>${p.title}</h3>

        <p>$${p.price}</p>

        <a href="${pageContext.request.contextPath}/customer/properties/detail?id=${p.id}">
          View
        </a>

      </div>

    </c:forEach>

  </div>

</div>