<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty sessionScope.currentUser}">
    <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}" />
</c:if>
