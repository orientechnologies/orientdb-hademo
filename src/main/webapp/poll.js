 var chart = c3.generate({
    bindto : "#chart",
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

var chart1 = c3.generate({
    bindto : "#chartWrite",
    data: {
        x: 'x',
        columns: [
            ['x'],
            ['Write'],
        ]
    },
    axis: {
        x: {
            type: 'timeseries',
            tick: {
                format: '%M:%S'
            }
        }
    }
});
var chart2 = c3.generate({
    bindto : "#chartRead",
    data: {
        x: 'x',

        columns: [
            ['x'],
            ['Read']
        ],
        colors: {
                Read: '#ff0000',
              },
    },
    axis: {
        x: {
            type: 'timeseries',
            tick: {
                format: '%M:%S'
            }
        }
    }
});


var counter = 0;
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

            var d = new Date();
            var formatDate = d.getMinutes() + "-"+ d.getSeconds();
            chart1.flow({
            columns : [
                ['x',new Date()],
                ['Write', obj.writeSec]
            ],
            length: 0,
            duration: 1500
          });
          chart2.flow({
                      columns : [
                          ['x',new Date()],
                          ['Read', obj.readSec]
                      ],
                      length: 0,
                      duration: 1500
                    });

           document.getElementById('readText').innerHTML = obj.readSec  + " read/sec";
           document.getElementById('writeText').innerHTML = obj.writeSec + " write/sec" ;

           counter++;
           if (counter == 60) {

                chart1.load({
                    columns : [
                              ['x',new Date()],
                              ['Write', 0]
                     ],
                    unload: ['x', "Write"]
                })
                chart2.load({
                    columns : [
                              ['x',new Date()],
                              ['Read', 0]
                               ],
                    unload: ['x', "Read"]
                 })
                 counter =0;
            }
    }
}
    xhttp.open("GET", "counts", true);
    xhttp.send();
    setTimeout(poll,1000);
}

poll();
