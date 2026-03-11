const revenueData = JSON.parse(document.getElementById("revenueData").value);
const transactionData = JSON.parse(document.getElementById("transactionData").value);

const months = revenueData.map(r => r.month);
const revenue = revenueData.map(r => r.revenue);

new Chart(
    document.getElementById('revenueChart'),
    {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: 'Revenue',
                data: revenue
            }]
        }
    }
);


const txCounts = transactionData.map(t => t.count);

new Chart(
    document.getElementById('transactionChart'),
    {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Transactions',
                data: txCounts
            }]
        }
    }
);