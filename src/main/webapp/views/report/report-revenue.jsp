<table border="1">
    <tr>
        <th>Month</th>
        <th>Revenue</th>
    </tr>

    <c:forEach var="row" items="${data}">
        <tr>
            <td>${row.month}</td>
            <td>${row.revenue}</td>
        </tr>
    </c:forEach>
</table>