function sendUserInfo() {
    var inputs = $(".user-detailed-info").serializeArray();

    // moment.locale("ru");
    var date = moment($("#birthday").val(), "DD MMMM, YYYY").utcOffset(0, true).toISOString().slice(0, -5);
    var data = {};
    inputs.forEach(function (e) {
        data[e.name] = e.value;
    });
    data['birthday'] = date;
    console.log(data);
    $.ajax({
        url: "/users/0",
        contentType: "application/json",
        method: "PUT",
        data: JSON.stringify(data)
           })
        .done(function () {
            document.location.href = '/user/0'
        })
}
