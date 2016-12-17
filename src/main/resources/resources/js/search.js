$("#search-form").submit(function (e) {
    e.preventDefault();
    var searchString = $("#search").val();
    searchString = $.trim(searchString);
    window.location.replace("/search?searchString=" + searchString);
});
