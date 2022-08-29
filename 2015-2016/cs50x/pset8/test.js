var background = document.getElementByID('background');
var button = document.getElementByID('party');

button.onclick = function() {
    var red = Math.floor(Math.random() * 255);
    var blue = Math.floor(Math.random() * 255);
    var green = Math.floor(Math.random() * 255);

    background.style.background = 'rgb('+ red', '+ green', '+ blue')';
};

var clock = document.getElementByID('clock');
setInterval(function() {
        // generate clock here // vie ntp       
        var date = new Date();
        var html = '';
        html += '<h1>' + date.getHours()  + ':' + date.getMinutes() + ':' +</h1> '
}, 100);




<!DOCTYPE html>
<html>
    <head 
       <style type = "text/css">
            html, body {height: 100%; margin: 0; padding: 0}
            #map {height: 100%}
        </style>
    </head>
    <body>
        <div id="map"></div>
        <script type="text/javascript">
            var map;
            function initMap(){
             map = new google.maps.Map(document.getElementById("map"), {
                center: {lat: -34.397, lng: 150.44},
                zoom: 8,
                mapTypeId: google.maps.MapTypeId.SATTELITE,
                heading: 90,
                tilt: 45
             });
             function rotate90 (){
                var heading = map.getHeading() || 0
  ;              map.setHeading(heading + 90);
             }
             function autoRotate() {
                if (map.getTilt() !== 0){
                    window.setInterval(rotate90, 3000);
                }
             }
            }
            map.setMapTypeId(google.maps.mapTypeId.SATTELITE);
            map.setTilt(0);
        </script>
        <script>
            <script async defer src="https://maps.googlapis.com/maps/api/js?key=###&callback=initMap">
        </script>
    </body>
</html>





// Modify the control to only display two maptypes, the
// default ROADMAP and the custom 'mymap'.
// Note that because this is simply an association, we
// don't need to modify the MapTypeRegistry beforehand.

var MY_MAPTYPE_ID = 'mymaps';

var mapOptions = {
  zoom: 12,
  center: brooklyn,
  mapTypeControlOptions: {
     mapTypeIds: [google.maps.MapTypeId.ROADMAP, MY_MAPTYPE_ID]
  },
  mapTypeId: MY_MAPTYPE_ID
};

// Create our map. This creation will implicitly create a
// map type registry.
map = new google.maps.Map(document.getElementById("map"),
    mapOptions);

// Create your custom map type using your own code.
// (See below.)
var myMapType = new MyMapType();

// Set the registry to associate 'mymap' with the
// custom map type we created, and set the map to
// show that map type.
map.mapTypes.set(MY_MAPTYPE_ID, myMapType);