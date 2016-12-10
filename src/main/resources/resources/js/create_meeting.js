/**
 * Created by Pavel on 27.11.2016.
 */
var map;
var markers = [];
$("#publish-button").click(function () {

    var values = [];

    //get fields with values from html-form
    $("#create-meeting-form").each(function () {
        values = $(this).serializeArray();
        event.preventDefault();
    });
    //get date-value pair
    var result = $.grep(values, function (e) {
        return e.name == "date";
    });
    var d = new Date(result[0].value);
    var date = d.toISOString().slice(0, 10);

    //get time-value pair
    result = $.grep(values, function (e) {
        return e.name == "time";
    })
    var time = result[0].value;

    //remove date & time objects from values
    values = values.filter(function( obj ) {
        return obj.name !== 'date' && obj.name !== 'time';
    });

    //add startDate field to json
    var startDate = date + "T" + time + ":00";
    values.push({name: 'start_date', value: startDate});

    values = values.filter(function (obj) {
        return obj.name != 'location';
    });

    if (markers[0] != null) {
        var lat = markers[0].getPosition().lat();
        var lng = markers[0].getPosition().lng();
        values.push({name: 'latitude', value: lat});
        values.push({name: 'longitude', value: lng});
    }
    var array = {};
    //convert array of object to object
    for (var i=0; i<values.length; i++) {
        array[values[i].name] = values[i].value;
    }
    $.ajax({
                type: 'POST',
                url: '/meetings',
                contentType: "application/json",
                data: JSON.stringify(array),
           }).done(function (result) {
        console.log(result);
        window.location.replace("/");
    }).fail(function (result) {
        console.log(result);
        console.log("fail");
    });
});


function initMap() {

    var myLatLng = {lat: 59.935946, lng: 30.321581};

    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLng,
        zoom: 15
    });

    map.addListener('click', function(e) {
        deleteMarkers();
        addMarker(e.latLng);
    });
}

// Adds a marker to the map and push to the array.
function addMarker(location) {
    var marker = new google.maps.Marker({
        position: location,
        map: map,
        draggable: true,
        animation: google.maps.Animation.DROP
    });
    markers.push(marker);
}

// Sets the map on all markers in the array.
function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
    setMapOnAll(null);
}

// Shows any markers currently in the array.
function showMarkers() {
    setMapOnAll(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
    clearMarkers();
    markers = [];
}
$(document).ready(function(){
    $('.modal').modal({
        ready: function (modal, trigger) {
            //map needs to resize because of modal
            google.maps.event.trigger(map, "resize");
        },
        complete: function () {
            if (markers[0] != null) {
                $.ajax({
                           url: "http://maps.googleapis.com/maps/api/geocode/json",
                           type: 'GET',
                           data: {latlng: markers[0].getPosition().lat()+","+ markers[0].getPosition().lng(), sensor: true},
                           success: function (response) {
                               setAddress(response);
                           },
                           error: function (xhr) {
                               console.log(xhr);
                           }
                       });
            }

        }
                      });
});

function setAddress(response) {
    var address = response.results[0]['formatted_address'].split(',');
    $("#location_input").val(address[0] + address[1] + ", " + address[2]);
    Materialize.updateTextFields();
};

$("#location").click(function () {
    $('#modal1').modal('open');
});
