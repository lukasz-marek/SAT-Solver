/**
 * Created by Tomek on 02.06.2016.
 */
$(document).ready(function () {
    $(".answer").hide();
    $(".head-button").children().hide();
    $(".con").children().hide();
    console.log("dziala");
    $("button").on("click", function () {
        if (this.id == "submit") {
            var formula = $("#formula").val();
            sat(formula);
            $(".answer").show();
        }else{
            $(".con").children().hide();
            console.log("p."+this.id);
            $("p#"+this.id).show();
        }

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
            $("button#form").show();
            $("p#form").text(res.formula);

            $("button#cnf").show();
            $("p#cnf").text(res.asCNF);


            $("span#satis").text("satisfiable: " + res.satisfiable);
            $("span#satis").show();
            if(res.satisfiable == true) {

                $("button#assig").show();
                $("p#assig").text(assignment(res.assignments));
            };
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