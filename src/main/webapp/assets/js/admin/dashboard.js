document.addEventListener("DOMContentLoaded", function () {


    document.querySelectorAll(".money").forEach(el => {

        let value = parseFloat(el.innerText);

        if (!isNaN(value)) {

            el.innerText =
                new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                }).format(value);

        }

    });


    const labels = [
        <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
            "${p.month}"${!loop.last ? "," : ""}
        </c:forEach>
    ];

    const data = [
        <c:forEach items="${dashboard.revenueChart.points}" var="p" varStatus="loop">
            ${p.revenue}${!loop.last ? "," : ""}
        </c:forEach>
    ];

    const ctx = document.getElementById('revenueChart');

    new Chart(ctx, {

        type: 'line',

        data: {

            labels: labels,

            datasets: [{

                label: 'Revenue',

                data: data,

                borderColor: '#2563eb',

                backgroundColor: 'rgba(37,99,235,0.2)',

                tension: 0.4,

                fill: true

            }]

        },

        options: {

            responsive: true,

            plugins: {

                legend: {
                    display: false
                }

            },

            scales: {

                y: {
                    beginAtZero: true
                }

            }

        }

    });

});