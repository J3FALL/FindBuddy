/**
 * Created by Pavel on 11.12.2016.
 */
var map;
var markers = [];

function initMap() {
    var lat = $("#map").attr("lat");
    var lng = $("#map").attr("lng");
    var myLatLng = {lat: parseFloat(lat), lng: parseFloat(lng)};
    console.log(myLatLng);
    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLng,
        zoom: 15
    });

    addMarker(myLatLng);
}

function addMarker(location) {
    var marker = new google.maps.Marker({
        position: location,
        map: map,
        draggable: false,
        animation: google.maps.Animation.DROP
    });
    markers.push(marker);
}
