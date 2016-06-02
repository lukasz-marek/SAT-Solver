/**
 * Created by Tomek on 02.06.2016.
 */
$(document).ready(function () {
    $("button").on("click", function () {
        var formula = $("#formula").val();
        sat(formula);
    });
});


function sat(form) {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/sat',
        headers: { formula : form }
    }).done(function(res){
        $(".list").append("<li> formula: "+form+ "  satisfiable: "+ res.satisfiable +" </li>");

    });


};