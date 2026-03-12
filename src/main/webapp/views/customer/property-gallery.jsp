<div class="gallery">

  <c:forEach var="img" items="${images}">

    <img src="${img.imageUrl}"
         class="gallery-img"/>

  </c:forEach>

</div>