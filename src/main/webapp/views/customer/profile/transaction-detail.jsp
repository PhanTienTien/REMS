<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/transactions.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/layout.css">

<div class="page-section">

    <div class="container">

        <div class="transaction-detail">

            <h2>Transaction Detail</h2>

            <table class="detail-table">

                <tr>
                    <td>ID</td>
                    <td>${transaction.id}</td>
                </tr>

                <tr>
                    <td>Property</td>
                    <td>${transaction.propertyTitleSnapshot}</td>
                </tr>

                <tr>
                    <td>Customer</td>
                    <td>${transaction.customerNameSnapshot}</td>
                </tr>

                <tr>
                    <td>Amount</td>
                    <td class="money">${transaction.amount}</td>
                </tr>

                <tr>
                    <td>Type</td>
                    <td>${transaction.type}</td>
                </tr>

                <tr>
                    <td>Status</td>
                    <td>${transaction.status}</td>
                </tr>

                <tr>
                    <td>Created At</td>
                    <td>${transaction.createdAt}</td>
                </tr>

                <tr>
                    <td>Completed At</td>
                    <td>${transaction.completedAt}</td>
                </tr>

                <tr>
                    <td>Processed By</td>
                    <td>${transaction.processedBy}</td>
                </tr>

            </table>

        </div>

    </div>

</div>


<%@ include file="../../common/footer.jsp" %>