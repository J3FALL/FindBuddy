
function subscribe(id) {
    var currentCategory = $('#' + id);
    console.log(currentCategory);
    var data = {
        id: id
    };
    console.log(JSON.stringify(data));
    $.ajax({
               type: 'POST',
               data: JSON.stringify(data),
               contentType: 'application/json',
               url: '/categories/subscribe'
           })
        .done(function (data) {
            currentCategory.text('Вы подписаны');
            currentCategory.attr('onclick', 'unsubscribe(' + id + ')');
        })
}

function unsubscribe(id) {
    var currentCategory = $('#' + id);
    console.log(currentCategory);
    var data = {
        id: id
    };
    console.log(JSON.stringify(data));
    $.ajax({
               type: 'POST',
               data: JSON.stringify(data),
               contentType: 'application/json',
               url: '/categories/unsubscribe'
           })
        .done(function (data) {
            currentCategory.text('Подписаться');
            currentCategory.attr('onclick', 'subscribe(' + id + ')');
        })
}
