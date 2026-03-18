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

    console.log("Labels:", revenueLabels);
    console.log("Data:", revenueData);

    const ctx = document.getElementById('revenueChart');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: revenueLabels,
            datasets: [{
                label: 'Revenue',
                data: revenueData,
                borderColor: '#2563eb',
                backgroundColor: 'rgba(37,99,235,0.2)',
                tension: 0.4,
                fill: true
            }]
        }
    });

});