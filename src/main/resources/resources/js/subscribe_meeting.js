$("#subscribe-meeting-button").click(function () {
    var meetingId = $("#subscribe-meeting-button").data("meeting");
    var urlPath = $("#subscribe-meeting-button").data("subscribed") ? "unsubscribe" : "subscribe";
    $.ajax({
        url: "/meetings/" + urlPath,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({id: meetingId})
           })
        .done(function () {
            var button = $("#subscribe-meeting-button");
           if (button.data("subscribed")) {
               button.data("subscribed", false);
               button.text("Подписаться");
           } else {
               button.data("subscribed", true);
               button.text("Вы подписаны");
           }
        });
});
