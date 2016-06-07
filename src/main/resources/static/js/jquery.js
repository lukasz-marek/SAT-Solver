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

        if(res.parseFailed == true){
           alert("error Code "+ res.errorCode +"  parser got instead " +
               ""+res.parserGotInstead + " error position " + res.parserErrorPosition )

        }else {

            if(res.satisfable == true) {
                $(".list").append("<li>satisfable: " + res.satisfable + " " + "formula: " + res.formula + " asCNF: " +
                    "" + res.asCNF + " " + assignment(res.assignments) + " </li>");
            }else{
                $(".list").append("<li>satisfable: " + res.satisfable + " " + "formula: " + res.formula + " asCNF: " +
                    "" + res.asCNF + "</li>");

            }
        };
    });


};

function assignment(assig){
    var string="";
    $.each(assig,function(index,value){
        string = string + index+ ": " + value+" ";

    });
    return string;
}

/*
parsefailed.
    Jeśli ma wartość true, to wyświetlasz errorCode i parserGotInstead.
(Errorcode będzie trzeba przerobić, każdemu numerkowi powinien towarzyszyć string, ale to później).
Dodatkowo: input formula powinno zostać wyświetlone.
    Jeśli natomiast parseFailed == false, ignorujesz parserGotInstead i errorcode.
    W zamian wyświetlasz input, asCnf, satisfiable.
    Jeśli satisfiable == true, wyświetlasz dodatkowo assignments.
(Bo jak satisfiable == false, to assignments wynosi null).
I to na razie byłoby tyle.


    */