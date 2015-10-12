var app = {
    count : 6,
    el : {
        self : document.getElementById('wrap'),
        width : document.getElementById('wrap').clientWidth,
        height: document.getElementById('wrap').clientHeight
    },
    circle : {
        radius : ( document.getElementById('wrap').clientWidth / 2 ) - 50,
        centerX : ( document.getElementById('wrap').clientWidth / 2 ),
        centerY : ( document.getElementById('wrap').clientHeight / 2 ),
    },
    methods : {
        circle : function(radius, steps, centerX, centerY) {
            var xValues = [centerX];
            var yValues = [centerY];
            for (var i = 0; i < steps; i++) {
                xValues[i] = (centerX + radius * Math.cos(2 * Math.PI * i / steps));
                yValues[i] = (centerY + radius * Math.sin(2 * Math.PI * i / steps));
            }

            return [xValues, yValues];
        }
    }
}

run(app);

function run(app){

    var coords = app.methods.circle(app.circle.radius, app.count, app.circle.centerX, app.circle.centerY);

    for ( var i=0; i<app.count; i++ ) {
        var x = coords[0][i],
            y = coords[1][i];

        var rand = Math.floor((Math.random() * app.count) + 1);

        var div = document.createElement('div')
        div.style.left = (x-25) + 'px'
        div.style.top = (y-25) + 'px'
        div.style.backgroundImage = 'url("http://api.randomuser.me/portraits/med/men/'+rand+'.jpg")'

        app.el.self.appendChild(div)
    }
}


function makeSmaller() {
    document.getElementById('wrap').innerHTML = "";
    document.getElementById('wrap').setAttribute("style","width:200px;height:200px");
    app.circle.radius = (document.getElementById('wrap').clientWidth / 2 ) - 50;
    app.circle.centerX = document.getElementById('wrap').clientWidth / 2;
    app.circle.centerY = document.getElementById('wrap').clientHeight / 2;

    run(app);
}
function makeBigger() {
    document.getElementById('wrap').innerHTML = "";
    document.getElementById('wrap').setAttribute("style","width:500px;height:500px");
    app.circle.radius = (document.getElementById('wrap').clientWidth / 2 ) - 50;
    app.circle.centerX = document.getElementById('wrap').clientWidth / 2;
    app.circle.centerY = document.getElementById('wrap').clientHeight / 2;

    run(app);
}


