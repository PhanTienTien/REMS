<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/bookings.css">

<div class="profile-page">

    <div class="profile-wrapper">

        <jsp:include page="components/profile-sidebar.jsp"/>

        <div class="profile-content">

            <div class="profile-card">

                <h2>BĐS đã đặt</h2>

                <table class="booking-table">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Property</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Note</th>
                        <th>Action</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach items="${bookings}" var="b">

                        <tr>

                            <td>${b.bookingId}</td>

                            <td>${b.propertyTitle}</td>

                            <td class="status ${b.status}">
                                    ${b.status}
                            </td>

                            <td>${b.createdAt}</td>

                            <td>${b.note}</td>

                            <td>

                                <c:if test="${b.status == 'PENDING'}">

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/customer/profile/bookings">

                                        <input type="hidden" name="action" value="cancel"/>
                                        <input type="hidden" name="id" value="${b.bookingId}"/>

                                        <button type="submit" class="btn-cancel">
                                            Cancel
                                        </button>

                                    </form>

                                </c:if>

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<%@ include file="../../common/footer.jsp" %>