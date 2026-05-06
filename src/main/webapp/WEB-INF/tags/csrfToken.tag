<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.rems.common.util.CsrfUtil" %>
<c:if test="${not empty sessionScope.currentUser}">
    <input type="hidden" name="_csrf" value="<%= CsrfUtil.getToken(session) %>" />
</c:if>
