/**
 * Created by Tomek on 02.06.2016.
 */
$(document).ready(function () {

    $(".answer").hide();
    $(".head-button").children().hide();
    $(".con").children().hide();


    $('form[name=cnf]').on('submit', function(e) {
        // Prevent form submission
        e.preventDefault();
        console.log("1 point");
        var $form    = $(e.target),
            formData = new FormData(),
            params   = $form.serializeArray(),
            files    = $form.find('[name="uploadedFiles"]')[0].files;
            console.log(files);
            console.log(params);
        console.log("2 point");
        $.each(files, function(i, file) {
            // Prefix the name of uploaded files with "uploadedFiles-"
            // Of course, you can change it to any string
            formData.append('file', file);
        });

        $.each(params, function(i, val) {
            formData.append(val.name, val.value);
        });
        console.log(formData.values());

        $.ajax({
            url: "http://localhost:8080/sat/dimacs",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(result) {
                $(".head-button").children().hide();
                show_file(result);
                console.log(result)
            }
        });
    });



    console.log("dziala");
    $("button").on("click", function () {
        if (this.id == "submit") {

            var formula = $("#formula").val();
            $(".head-button").children().hide();
            sat(formula);

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
        show_sat(res);
        });

};

function assignment(assig){
    var string="";
    $.each(assig,function(index,value){
        string = string + index+ ": " + value+"          " ;

    });
    return string;
}

function map_error(num) {
    switch (num) {
        case 1:
            return "alphanumeric";
        case 4:
            return "and operator";
        case 5:
            return "or operator";
        case 7:
            return "implication end";
        case 9:
            return "if and only if continue";
        case 10:
            return "if and only if end";
        case 12:
            return "alphanumeric or open bracket or not operator";
        case 13:
            return "alphanumeric or close bracket or any operator";
        case 14:
            return "close bracket or operator";
        case 15:
            return "closing unopened bracket"
        case 16:
            return "bracket was opened but not closed"
    }
}
function show_sat(res){
    if(res.parseFailed == true){
        alert("Error name: "+ map_error(res.errorCode) +"  parser got instead " +
            ""+res.parserGotInstead + " error position " + res.parserErrorPosition )
    }else {

        $(".answer").show();

        $("button#form").show();
        $("p#form").text(res.formula);

        $("button#cnf").show();
        $("p#cnf").text(res.asCNF);


        $("span#satis").text("satisfiable: " + res.satisfiable);
        $("span#satis").show();
        if(res.satisfiable == true) {

            $("button#assig").show();
            $("p#assig").text(assignment(res.assignments));
            $(".answer").show();
        };
    };
}function show_file(res){
    if(res.parseFailed == true){
        alert("Error name: "+ map_error(res.errorCode) +"  parser got instead " +
            ""+res.parserGotInstead + " error position " + res.parserErrorPosition )
    }else {
        if(res.satisfiable == true) {
            $("span#satis").text("satisfiable: " + res.satisfiable);
            $("span#satis").show();
            $("button#assig").show();
            $("p#assig").text(assignment(res.assignments));
            $("p#assig").show();
            $(".answer").show();
        };
    };
}
/*
$(":file").change(function(){
    var filename = $('input[type=file]').val().replace(/C:\\fakepath\\/i, '')
    console.log(filename);
    $.ajax({
        url: 'localhost:8080/sat/dimacs',
        type: 'post',
        data: filename,
        dataType: 'file',
        success: function(data){
            console.log("ok")
        }
    })
}); */