 var chart = c3.generate({
    data: {
        columns: [
            ['completed', 0],
            ['empty', 360]
        ],
        type: 'pie',
        colors: {
            empty: '#ffffff',
          },

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

        document.getElementById('text').innerHTML = obj.count ;
        var percentage = ( (obj.count) / 100 ) % 360;
         var toLoad = 360 - percentage;
          chart.load({
                columns: [['empty', toLoad],['completed', percentage]]
            });
    }
}
    xhttp.open("GET", "counts", true);
    xhttp.send();
    setTimeout(poll,200);
}

poll();
