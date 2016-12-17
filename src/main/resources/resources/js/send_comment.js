$(".send-comment").click(function () {
    var meetingId = $(".send-comment").data('meeting');
    var text = $("#new-comment-text").val();
    var newComment = {
        meetingId: meetingId,
        text: text
    };
    $.ajax({
               url: "/comments",
               contentType: "application/json",
               method: "POST",
               data: JSON.stringify(newComment)
           })
        .done(function (data) {
            drawNewComment(data);
            $("#new-comment-text").val('');
        })
});

function drawNewComment(newComment) {
    var source = $('#comment-template').html();
    var template = Handlebars.compile(source);
    console.log(moment(newComment.date));
    var date = moment(newComment.date).locale("ru").utcOffset(0, true).format("DD MMM HH:mm");
    var context = {
        authorLink: "/user/" + newComment.authorId,
        authorPhoto: newComment.authorPhoto != null ? "/icons/" + newComment.authorPhoto :
                     "/icons/no_photo.png" ,
        authorName: newComment.authorName,
        commentText: newComment.text,
        commentDate: date
    };
    var html = template(context);
    $(html).hide().appendTo(".comment-container").slideDown("fast");
}