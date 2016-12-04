$("a[id='input-btn']").click(function () {
    $("input[id='file-input']").trigger('click');
});

var userPhoto =  $("#user-photo");

function uploadFile() {
    $("#error").hide();
    var photo = document.getElementById("file-input");
    // the file is the first element in the files property
    var formData = new FormData();
    var file = photo.files[0];
    console.log(file.size / 1024 / 1024);
    if ((file.size / 1024 / 1024) > 2) {
        $("#size-error").show();
        return;
    }
    else {
        $("#size-error").hide();
    }
    formData.append("file", file);

    $.ajax({
               url: "/upload_image",
               data: formData,
               dataType: 'text',
               processData: false,
               contentType: false,
               type: 'POST'
           })
        .done(function (data) {
            if (data == 'File is empty') {
                $("#error").html("File is empty.").show();
                return;
            }
            if (data == 'Not valid image') {
                $("#error").html("Not valid image.").show();
                return;
            }
            var newPhoto = '/images/' + data;
            userPhoto.attr("src", newPhoto);
        });
    $("#file-input").val("");
    return false;
}

function deleteFile() {
    $.ajax({
        url: "/delete_image",
        type: "DELETE"
           });
    userPhoto.attr("src", "/images/no_photo.png");
}