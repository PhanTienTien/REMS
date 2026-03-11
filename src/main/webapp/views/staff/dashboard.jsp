<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>REMS Staff Dashboard</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

</head>

<body>

<div class="dashboard-container">

    <jsp:include page="../staff/components/sidebar.jsp"/>

    <div class="main-content">

        <jsp:include page="../staff/components/topbar.jsp"/>

        <div class="dashboard-content">

            <h2>My Dashboard</h2>

            <div class="cards-container">

                <div class="card">

                    <div class="card-title">
                        My Draft Properties
                    </div>

                    <div class="card-value">
                        ${dashboard.myDraftProperties}
                    </div>

                </div>

                <div class="card">

                    <div class="card-title">
                        My Active Properties
                    </div>

                    <div class="card-value">
                        ${dashboard.myActiveProperties}
                    </div>

                </div>

                <div class="card">

                    <div class="card-title">
                        My Transactions
                    </div>

                    <div class="card-value">
                        ${dashboard.myTransactions}
                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>