
const ctx = document.getElementById('lineChart').getContext('2d');

const rentData = {
    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    datasets: [{
        label: "Rent Collected (₹)",
        data: [1200, 1500, 1800, 1600, 2000, 2200, 2100, 2400, 2600, 2300, 2500, 2800],
        borderWidth: 2,
        tension: 0.4,
        fill: false
    }]
};

const config = {
    type: 'line',
    data: rentData,
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: true
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: "Rent (₹)"
                }
            },
            x: {
                title: {
                    display: true,
                    text: "Month"
                }
            }
        }
    }
};

new Chart(ctx, config);
