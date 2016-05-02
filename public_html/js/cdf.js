/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var data = {
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
        {
            label: "My First dataset",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "tomato",
            borderColor: "red",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "rgba(75,192,192,1)",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(75,192,192,1)",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: [.65, .59, .80, .81, .56, .55, .40],
        },
        {
            label: "My Second dataset",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "springgreen",
            borderColor: "green",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "rgba(75,192,192,1)",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(75,192,192,1)",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: [0.52, 0.2, 0.25, 1, 0.66, 0.24, 0.80],
        }
    ]
};


var options = {
    scales: {
        yAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: 'probability'
                }
            }],
        yAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: 'Average Response Time (ms)'
                }
            }]
    }
}
var ctx = $("#cdf");
var myLineChart = new Chart(ctx, {
    type: 'line',
    data: data,
    options: options
});

