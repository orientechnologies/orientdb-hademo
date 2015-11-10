

 var chart = c3.generate({
    data: {
        columns: [
            ['data', 0]
        ],
        type: 'gauge',
        onclick: function (d, i) { console.log("onclick", d, i); },
        onmouseover: function (d, i) { console.log("onmouseover", d, i); },
        onmouseout: function (d, i) { console.log("onmouseout", d, i); }
    },
    gauge: {

    },
    color: {
        pattern: ['#FF0000', '#F97600', '#F6C600', '#60B044'], // the three color levels for the percentage values.
        threshold: {
            values: [30, 60, 90, 100]
        }
    },
    size: {
        height: 180
    }



});


var poll = function(){
    var xhttp = new XMLHttpRequest();


    xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
        var obj = JSON.parse(xhttp.responseText);

        document.getElementById('text').innerHTML = obj.count + "/" + obj.limit;
        var percentage = obj.count * 100 / obj.limit;

          chart.load({
                columns: [['data', percentage]]
            });
    }
}
    xhttp.open("GET", "counts", true);
    xhttp.send();
    setTimeout(poll,1000);
}

poll();

