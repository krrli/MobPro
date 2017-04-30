function showMovies(){
    var movieToFind = document.getElementById("txtSearch").value;

    $.ajax({
        url: 'http://www.omdbapi.com/?y=&plot=short&r=json&t=' + movieToFind,
        dataType: 'jsonp',
        jsonp: 'callback',
        jsonpCallback: 'machOepis',
        error: function () {
            alert("there's something wrong...");
        }
    });

    //alert('Result: '+JSON.stringify(result))
}

function machOepis(data){
    if (data.Response == "False") {
        alert("there's something wrong...");
        return;
    }

    alert(data.Title + " "  + data.Year);

}