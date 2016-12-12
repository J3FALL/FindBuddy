function sendUserInfo() {
    var inputs = $(".user-detailed-info").serializeArray();

    moment.locale("ru");
    var date = moment($("#birthday").val(), "DD MMMM, YYYY").utcOffset(0, true).toISOString().slice(0, -5);
    var data = {};
    var allValid = true;
    inputs.forEach(function (e) {
        if (e.value.localeCompare("") == 0 && e.name.localeCompare('description') != 0) {
            $("input[name=" + e.name + "]").addClass("invalid");
            allValid = false;
        }
        else {
            $("input[" + e.name + "]").removeClass("invalid");
        }
        data[e.name] = e.value;
    });
    data['birthday'] = date;
    if (allValid) {
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
}
function deleteUserProfile() {
    $.ajax({
        url: "/users/0",
        contentType: "application/json",
        method: "DELETE"
           })
        .done(function (data) {
            if (data.message == "Successful." && data.error == null) {
                window.location.replace("/logout");
            }
        })
}