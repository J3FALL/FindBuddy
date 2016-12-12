$("#search-form").submit(function (e) {
    e.preventDefault();
   var searchString = $("#search").val();
    window.location.replace("/search?searchString=" + searchString);
});
