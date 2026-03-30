<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/transactions.css">

<div class="profile-page">

    <div class="profile-wrapper">

        <jsp:include page="components/profile-sidebar.jsp"/>

        <div class="profile-content">

            <div class="profile-card">

                <h2>Giao dịch của bạn</h2>

                <table class="transaction-table">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Property</th>
                        <th>Amount</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="tx" items="${transactions}">

                        <tr>

                            <td>${tx.id}</td>

                            <td>${tx.propertyTitleSnapshot}</td>

                            <td class="money">$${tx.amount}</td>

                            <td>${tx.type}</td>

                            <td>

<span class="badge
<c:choose>
<c:when test="${tx.status == 'COMPLETED'}">badge-completed</c:when>
<c:when test="${tx.status == 'PENDING'}">badge-pending</c:when>
<c:otherwise>badge-failed</c:otherwise>
</c:choose>
">

        ${tx.status}

</span>

                            </td>

                            <td>${tx.createdAt}</td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<%@ include file="../../common/footer.jsp" %>