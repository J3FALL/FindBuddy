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
    moment.locale("ru");
    values['birthday'] = moment(values['birthday'], "DD MMMM, YYYY").utcOffset(0, true).toISOString().slice(0, -5);
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
        var responseText = JSON.parse(result.responseText);
        console.log(responseText.error);
        if ('UserAlreadyExist' == responseText.error) {
            $("#email-label").text(responseText.message).show();
            $("#email").removeClass("valid").addClass("invalid");
        }
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
        else {
            $('#' + key).removeClass("invalid");
        }
    });
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
        $("#email-label").hide();
    }
    return isAllValid;
}

