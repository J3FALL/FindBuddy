var password = $("#password")
    , confirm_password = $("#password-again")
    , email = $("#email")
    , birthday = $("#birthday");

var emailRegEx = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

var isAllValid = false;

function validatePassword() {
    if(password.val().length < 6){
        password.removeClass("valid").addClass("invalid");
    }
    else {
        password.removeClass("invalid").addClass("valid");
    }
    if (password.val() != confirm_password.val()) {
        confirm_password.addClass("invalid");
    } else {
        confirm_password.removeClass("invalid").addClass("valid");
    }
}

password.change(validatePassword);
confirm_password.keyup(validatePassword);

$("#registration-button").click(function () {
    var $inputs = $('#registration-form :input');
    var values = {};
    $inputs.each(function () {
        if (this.id != "") {
            values[this.id] = this.value;
        }
    });
    validateInputs(values);
    if (!isAllValid) {
        return;
    }
    var date = new Date(values['birthday']);
    date.setUTCMinutes(date.getUTCMinutes() - date.getTimezoneOffset());
    values['birthday'] = date.toISOString().slice(0, -1);
    values = JSON.stringify(values);
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

function validateInputs(values) {
    var keys = Object.keys(values);
    isAllValid = true;
    keys.forEach(function (key) {
        if (values[key].localeCompare("") == 0) {
            $('#' + key).addClass("invalid");
            isAllValid = false;
        }
    });
    var timestamp = Date.parse(values['birthday']);
    var birthday = $("#birthday");
    if (isNaN(timestamp) == true) {
        birthday.removeClass("valid");
        birthday.addClass("invalid");
        isAllValid = false;
    }
    else {
        birthday.removeClass("invalid");
        birthday.addClass("valid");
    }
    if (values['password'].localeCompare(values['password-again']) != 0 || values['password'].length < 6) {
        password.removeClass("valid");
        password.addClass("invalid");
        isAllValid = false;
    }
    if (values['email'].match(emailRegEx) == null) {
        $("#email").addClass("invalid");
        isAllValid = false;
    }
    else {
        email.addClass("valid");
        email.removeClass("invalid");
    }
    return isAllValid;
}

