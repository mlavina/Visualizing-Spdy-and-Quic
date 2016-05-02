/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$.getJSON("resources/data.json", function (data) {
    
    var numSpdy = 0;
    var numQuic = 0;
    var numHttp = 0;
    
    $(data).each(function (index){
        if(this.spdy && this.quic){
            numQuic++;
        }else if(this.spdy){
            numSpdy++;
        }else{
            numHttp++;
        }
    });
    var data = {
        labels: [
            "HTTP",
            "Spdy+Quic",
            "Spdy"
        ],
        datasets: [
            {
                data: [numHttp, numQuic, numSpdy],
                backgroundColor: [
                    "#FF6384",
                    "#00FF7F",
                    "#FFCE56"
                ],
                hoverBackgroundColor: [
                    "#FF6384",
                    "#00FF7F",
                    "#FFCE56"
                ]
            }]
    };

    var ctx = $("#myChart");

    var myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: data,
        animation: {
            animateScale: true
        }
    });

});