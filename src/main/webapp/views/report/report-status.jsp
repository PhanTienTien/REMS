<table border="1">
    <tr>
        <th>Status</th>
        <th>Total</th>
    </tr>

    <c:forEach var="row" items="${data}">
        <tr>
            <td>${row.status}</td>
            <td>${row.total}</td>
        </tr>
    </c:forEach>
</table>