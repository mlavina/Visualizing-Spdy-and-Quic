/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$.getJSON("resources/data.json", function (data) {


    var now = moment().minutes(0).seconds(0).milliseconds(0);


// create a data set with groups
    var groups = new vis.DataSet();

    $(data).each(function (index) {
        var color = 'tomato';
        if(this.spdy && this.quic){
            color = 'green';
        }else if(this.spdy){
            color  = 'yellow';
        }
        groups.add({id: index, 
            content: this.siteName, 
            numPackets: this.objects.length, 
            alexaRank : this.alexaRank, 
            throughput : this.throughput,
            pageLoadTime : this.pageLoadTime,
            avgResponseTime : this.avgResponseTime,
            style:'background-color:' + color});
    });

    // create a dataset with items
    var items = new vis.DataSet();
    var idIndex = 0;
    $(data).each(function (index) {
        if (this.objects !== null) {
            $(this.objects).each(function (i) {
                var start = now.clone().add(this.requestTimestamp, 'milliseconds');
                var end = now.clone().add(this.responseTimestamp, 'milliseconds');
                var group = index;
                var file = this.fileType;
                if(file === null || file === ""){
                    file = " ";
                }
                items.add({
                    id: idIndex++,
                    group: group,
                    content: file,
                    start: start,
                    end: end,
                    type: 'range',
                    numOfBytes: this.numOfBytes
                });
            });
        }
    });
    /*
    for (var i = 0; i < itemCount; i++) {
        var start = now.clone().add(Math.random() * 200, 'milliseconds');
        var end = start.clone().add(Math.random() * 10, 'milliseconds');

        var group = Math.floor(Math.random() * groupCount);
        items.add({
            id: i,
            group: group,
            content: 'item ' + i +
                    ' <span style="color:#97B0F8;">(' + names[group] + ')</span>',
            start: start,
            end: end,
            type: 'range',
            size: Math.random() * 1024
        });
    }*/

// create visualization
    var container = document.getElementById('visualization');
    var options = {
        groupOrder: 'content', // groupOrder can be a property name or a sorting function
        min: now
    };

    var timeline = new vis.Timeline(container);
    timeline.setOptions(options);
    timeline.setGroups(groups);
    timeline.setItems(items);


    timeline.on('click', function (properties) {
        logEvent('click', properties);
    });
    function logEvent(event, properties) {
        if (properties.item !== null) {
            var item = items.get()[properties.item];
            var group = groups.get()[properties.group];
            var w = 250;
            var h = 200;
            var left = (screen.width/2)-(w/2);
            var top = (screen.height/2)-(h/2);
            var myWindow = window.open("", group.content, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
            myWindow.document.title = group.content;
            myWindow.document.write('<title>' +group.content+'</title>');
            myWindow.document.write("<h3>" + group.content + "</h3>");
            var respTime = item.end.diff(item.start, 'milliseconds');
            myWindow.document.write("<p>ResponseTime " + respTime + " milliseconds</p>");
            myWindow.document.write("<p>Filetype:" + item.content + "</p>");
            myWindow.document.write("<p>Size:" + item.numOfBytes + " bytes</p>");
        }else if(properties.what === "group-label"){
            var group = groups.get()[properties.group];
            var w = 350;
            var h = 300;
            var left = (screen.width/2)-(w/2);
            var top = (screen.height/2)-(h/2);
            var myWindow = window.open("", group.content, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
            myWindow.document.title = group.content;
            myWindow.document.write('<title>' +group.content+'</title>');
            myWindow.document.write("<h3>" + group.content + "</h3>");
            myWindow.document.write("<p>Alexa Rank: " + group.alexaRank + "</p>");
            myWindow.document.write("<p>Number of Requests: " + group.numPackets + "</p>");
            myWindow.document.write("<p>Average RTT: " + group.avgResponseTime + " ms</p>");
            myWindow.document.write("<p>Page Load Time: " + group.pageLoadTime + " ms</p>");
            myWindow.document.write("<p>Throughput: " + group.throughput * 1000 + " bytes/sec</p>");
        }
    }
});


