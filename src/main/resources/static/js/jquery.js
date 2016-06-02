/**
 * Created by Tomek on 02.06.2016.
 */
$(document).ready(function () {
    $("button").on("click", function () {
        var formula = $("#formula").val();
        console.log(formula);
        sat(formula);
    });
});


function sat(form) {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/sat',
        headers: { formula : form }
    }).done(function(res){
        alert("succes?   "+res.satisfiable);
    });


};