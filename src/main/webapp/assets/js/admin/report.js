if (!revenueData || revenueData.length === 0) {
    console.warn("No data for chart");
} else {

    const months = revenueData.map(r => r.month);
    const revenue = revenueData.map(r => r.revenue);
    const txCounts = revenueData.map(r => r.count);

    new Chart(document.getElementById('revenueChart'), {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: 'Revenue',
                data: revenue
            }]
        }
    });

    new Chart(document.getElementById('transactionChart'), {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Transactions',
                data: txCounts
            }]
        }
    });
}