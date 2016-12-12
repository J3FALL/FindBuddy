/**
 * Created by Pavel on 11.12.2016.
 */
var map;
var markers = [];
var lang = {};
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
    console.log("!");
    //validate();
    if ($("#create-meeting-form").valid() === false) {
        return;
    } else {
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

        moment.locale("ru");
        var date = moment(result[0].value, "DD MMMM, YYYY").utcOffset(0, true).toISOString().slice(0, -5);
        console.log(date);
        /*
         //here comes some shit code
         var str = result[0].value.split(" ");
         str[1] = lang[str[1].replace(",", "")];
         var d = new Date(str);
         var date = d.toISOString().slice(0, 10);*/

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
        //convert array of object to object
        for (var i=0; i<values.length; i++) {
            array[values[i].name] = values[i].value;
        }
        id = $("#publish-button").attr("meeting");
        console.log(id);
        $.ajax({
                   type: 'PUT',
                   url: '/meetings/' + id,
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

});


function initMap() {
    var defaultLatLng = {lat: 59.935946, lng: 30.321581};
    var lat = $("#map").attr("lat");
    var lng = $("#map").attr("lng");
    console.log(lat);
    var myLatLng = {lat: parseFloat(lat), lng: parseFloat(lng)};
    var myLatLngCenter = {lat: parseFloat(lat) + 0.008,  lng: parseFloat(lng) - 0.02};
    console.log(myLatLng);
    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLngCenter,
        zoom: 14
    });
    addMarker(myLatLng);
    getAddress();
    setAddress();
    map.addListener('click', function(e) {
        deleteMarkers();
        addMarker(e.latLng);
    });

    map.setCenter(myLatLng);
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
    Materialize.updateTextFields();
    $('.modal').modal({
                          ready: function (modal, trigger) {
                              //map needs to resize because of modal
                              google.maps.event.trigger(map, "resize");
                          },
                          complete: function () {
                              if (markers[0] != undefined && markers[0] != null) {
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

    jQuery.extend(jQuery.validator.messages, {
        required: 'Недопустимое значение',
        remote: 'Недопустимое значение'
    });
    $.validator.setDefaults({
                                errorClass: 'invalid',
                                validClass: "valid",
                                ignore: [],
                                errorPlacement: function (error, element) {
                                    $(element)
                                        .closest("form")
                                        .find("label[for='" + element.attr("id") + "']")
                                        .attr('data-error', error.text());
                                },
                                submitHandler: function (form) {
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

$("#cancel-button").click(function () {
    console.log("!");
    window.location.assign('/');
});

function getAddress() {
    if (markers[0] != undefined && markers[0] != null) {
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
