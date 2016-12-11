/**
 * Created by Pavel on 11.12.2016.
 */
var map;
var markers = [];

function initMap() {

    /*lat = [[$(meeting).latitude]];
    lng = [[$(meeting).longitude]];*/

    var myLatLng = {lat: 59.935946, lng: 30.321581};
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
        draggable: true,
        animation: google.maps.Animation.DROP
    });
    markers.push(marker);
}
