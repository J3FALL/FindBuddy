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
    // $('#login-form').serializeArray();

    $.ajax({
               type: 'POST',
               url: '/registration',
               data: values
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
