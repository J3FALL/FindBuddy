$("#registration-button").click(function () {
    // var accountdto = {
    //     name: $("#name"),
    //     surname: $("#surname"),
    //     password: $("#password")
    // };
    var $inputs = $('#login-form :input');
    // console.log($inputs[0].value);

    var values = {};
    $inputs.each(function () {
        if (this.id != "") {
            values[this.id] = this.value;
        }
    });
    var date =  new Date(values['birthday']);
    console.log(date.getTimezoneOffset());
    date.setUTCMinutes(date.getUTCMinutes() - date.getTimezoneOffset());
    values['birthday'] = date.toISOString().slice(0, -1);
    values = JSON.stringify(values);

    // $('#login-form').serializeArray();

    $.ajax({
               type: 'POST',
               url: '/users',
               data: values,
        contentType: 'application/json'
           }).done(function (result) {
        console.log(result);
        window.location.replace("/login");
    }).fail(function (result) {
        console.log(result);
        console.log("fail")
    });

});

function alo(data) {
    console.log(data)
}
