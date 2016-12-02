/**
 * Created by Pavel on 27.11.2016.
 */
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

$(document).ready(function(){
    // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
    $('.modal').modal();
});

$("#location_input").click(function () {
    $('#modal1').modal('open');
})