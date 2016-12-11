var locale = 'ru-ru';
var dateOption = {day : 'numeric', month: 'long', timeZone : 'Europe/Moscow'};
var timeOption = {hour: '2-digit', minute: '2-digit'};
var meetings = [];
var newContentBlock = $(".new-content");
setTimeout(getNewMeetings, 5000);

function getNewMeetings() {
    $.ajax({
               type: 'GET',
               url: '/meetings/new_meetings'
           })
        .done(function (data) {
            if (data != null && data.length != 0) {
                meetings.push.apply(meetings, data);
                newContentBlock.show(600);
                // drawNewMeetings(data);
            }
            setTimeout(getNewMeetings, 5000);
        })
}

function showNewContent() {
    newContentBlock.hide(600);
    drawNewMeetings();
    meetings = [];
}


var drawNewMeetings = function () {
    var source = $('#meeting-template').html();
    var template = Handlebars.compile(source);
    var main = $("#main-content");
    meetings.forEach(function (meeting) {
        // var meeting = $(this);
        var startDate = new Date(meeting.start_date);
        startDate.setUTCMinutes(startDate.getUTCMinutes() + startDate.getTimezoneOffset());
        var date = startDate.toLocaleDateString(locale, dateOption);
        var time = startDate.toLocaleTimeString(locale, timeOption);
        console.log(time);
        var context = {
            id : meeting.id,
            meetingLink: "/meeting/" + meeting.id,
            categoryName: meeting.category_name,
            title: meeting.title,
            startDate: date,
            startTime: time,
            stationName: meeting.station_name,
            subscribersNum: meeting.subscribersNum
        };
        var html = template(context);
        $(html).hide().prependTo("#main-content").slideDown("slow");
    })
};

$(window).bind('beforeunload', function () {
    $.ajax({
               type: 'GET',
               url: '/users/leave_feed'
           })
});
