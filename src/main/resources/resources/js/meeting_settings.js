$.ajax({
           url: "http://maps.googleapis.com/maps/api/geocode/json",
           type: 'GET',
           data: {
               latlng: $("#map").data("lat") + ","
                       + $("#map").data("lng"),
               sensor: true
           },
           success: function (response) {
               setAddress(response);
           },
           error: function (xhr) {
               console.log(xhr);
           }
       });
var map;
var service;
var markers = [];
var lang = {};
var mapCoordinatesChange = false;
lang["Январь"] = "January";
lang["Февраль"] = "February";
lang["Март"] = "March";
lang["Апрель"] = "April";
lang["Май"] = "May";
lang["Июнь"] = "June";
lang["Июль"] = "July";
lang["Август"] = "August";
lang["Сентябрь"] = "September";
lang["Октябрь"] = "October";
lang["Ноябрь"] = "November";
lang["Декабрь"] = "December";

$("#publish-button").click(function () {
    //validate();
    var values;
    //get fields with values from html-form
    $("#create-meeting-form").each(function () {
        values = $(this).serializeArray();
        event.preventDefault();
    });
    //get date-value pair
    var result = $.grep(values, function (e) {
        return e.name == "date";
    });

    moment.locale("ru");
    var date = moment(result[0].value, "DD MMMM, YYYY").utcOffset(0, true).toISOString()
        .slice(0, -5);
    console.log(date);
    //get time-value pair
    result = $.grep(values, function (e) {
        return e.name == "time";
    });
    var time = result[0].value;
    //remove date & time objects from values
    values = values.filter(function (obj) {
        return obj.name !== 'date' && obj.name !== 'time';
    });
    //add startDate field to json
    var startDate = date.toString().slice(0, 10) + "T" + time + ":00";
    values.push({name: 'start_date', value: startDate});

    values = values.filter(function (obj) {
        return obj.name != 'location';
    });
    if (markers[0] != undefined && markers[0] !== null) {
        var lat = markers[0].getPosition().lat();
        var lng = markers[0].getPosition().lng();
        values.push({name: 'latitude', value: lat});
        values.push({name: 'longitude', value: lng});
    }
    var array = {};
    var allValid = true;
    //convert array of object to object
    for (var i = 0; i < values.length; i++) {
        array[values[i].name] = values[i].value;
        $(values[i].name).removeClass("invalid");
        if (values[i].value == "" && values[i].name != 'description') {
            allValid = false;
            $("input[name=" + values[i].name + "]").addClass("invalid");
        }
        else {
            $("input[name=" + values[i].name + "]").removeClass("invalid");
        }
        if (values[i].name == "category_id") {
            if (values[i].value == "choose-category") {
                allValid = false;
                $(".select-dropdown").css("border-bottom", "0");
                $(".select-dropdown").css("border-bottom", "1px solid #F44336");
            } else {
                $(".select-dropdown").removeClass("invalid");
                $(".select-dropdown").css("border-bottom", "1px solid #9e9e9e");
                $(".select-dropdown").css("box-shadow", "");
                $(".select-wrapper input.select-dropdown").css("border-bottom", "");
            }
        }
    }
    if ($("#location").val() == "") {
        allValid = false;
        $("#location").css("border-bottom", "1px solid #F44336");
    }
    else {
        $("#location").css("border-bottom", "1px solid #9e9e9e");
    }

    if ($("#datepicker").val() == "") {
        allValid = false;
        $("#datepicker").css("border-bottom", "1px solid #F44336");
    }
    else {
        $("#datepicker").css("border-bottom", "1px solid #9e9e9e");
    }

    if ($("#timepicker").val() == "") {
        allValid = false;
        $("#timepicker").css("border-bottom", "1px solid #F44336");
    }
    else {
        $("#timepicker").css("border-bottom", "1px solid ");
    }

    if ($("#station").val() == "") {
        allValid = false;
        $("#station").css("border-bottom", "1px solid #F44336");
    } else {
        $("#station").css("border-bottom", "1px solid #9e9e9e");
        array["station"] = ($("#station").val());
    }
    if (!mapCoordinatesChange) {
        array['latitude'] = $("#map").data("lat");
        array['longitude'] = $("#map").data("lng");
    }

    if (allValid == true) {
        $.ajax({
                   type: 'PUT',
                   url: '/meetings/' + $("#publish-button").attr("meeting"),
                   contentType: "application/json",
                   data: JSON.stringify(array)
               }).done(function (result) {
            console.log(result);
            window.location.replace("/");
        }).fail(function (result) {
            console.log(result);
            console.log("fail");
        });
    }

})
;

function initMap() {

    var myLatLng = {lat: $("#map").data("lat"), lng: $("#map").data("lng")};

    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: myLatLng.lat + 0.003,
            lng: myLatLng.lng - 0.0095
        },
        zoom: 15
    });

    addMarker(myLatLng);

    map.addListener('click', function (e) {
        deleteMarkers();
        addMarker(e.latLng);
    });

    service = new google.maps.places.PlacesService(map);
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
    mapCoordinatesChange = true;
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
$(document).ready(function () {
    $("#station").attr("tabIndex", "-1");
    $(".station-wrapper").attr("tabIndex", "-1");
    $('.modal').modal(
        {
            ready: function (modal, trigger) {
                //map needs to resize because of modal
                google.maps.event.trigger(map, "resize");
            },
            complete: function () {
                if (markers[0] != undefined && markers[0] != null) {
                    $.ajax({
                               url: "http://maps.googleapis.com/maps/api/geocode/json",
                               type: 'GET',
                               data: {
                                   latlng: markers[0].getPosition().lat() + ","
                                           + markers[0].getPosition().lng(),
                                   sensor: true
                               },
                               success: function (response) {
                                   $("#station").val("");
                                   setAddress(response);
                                   var request = {
                                       location: markers[0].getPosition(),
                                       rankby: 'distance',
                                       types: ['subway_station'],
                                       sensor: true
                                   };
                                   service.textSearch(request, function (results, status) {
                                       console.log(results);
                                       $("#station").val(results[0].name);
                                       Materialize.updateTextFields();
                                   });
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
    $("#location").val(address[0] + address[1] + ", " + address[2]);
    Materialize.updateTextFields();
}

$("#location").click(function () {
    $('#modal1').modal('open');
});

$("#cancel-button").click(function () {
    console.log("!");
    window.location.assign('/');
});




