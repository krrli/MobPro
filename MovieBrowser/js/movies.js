function showMovies(){
    var movieToFind = document.getElementById("txtSearch").value;

    $.ajax({
        url: 'http://www.omdbapi.com/?y=&plot=short&r=json&t=' + movieToFind,
        dataType: 'jsonp',
        jsonp: 'callback',
        jsonpCallback: 'foellDasAb',
        error: function () {
            alert("nüt gfonde...");
        }
    });

    //alert('Result: '+JSON.stringify(result))
}

function foellDasAb(data){
    if (data.Response == "False") {
        alert("nüt gfonde...");
        return;
    }

    $("#txtTitle").val();
    $("#txtYear").val();
    $("#txtActors").empty();
    $("#txtPlot").empty();
    $("#txtRating").val();


    $("#txtTitle").val(data.Title);
    $("#txtYear").val(data.Year);
    $("#txtActors").val(data.Actors);
    $("#txtPlot").val(data.Plot);
    $("#txtRating").val(data.Ratings[0].Value);
    $("#imgPoster").attr("src", data.Poster);

    $("body").pagecontainer("change", "#page-movie", { transition: "flip" });
}