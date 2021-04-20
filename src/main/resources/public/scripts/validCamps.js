function validate() {
    var f1 = document.getElementById("fullName");
    var f2 = document.getElementById("sector");
    var username = document.getElementById("username");
    var password = document.getElementById("password");
    var academicLevel = "";
    academicLevel = $("#academicLevel").val()
    var f3 = document.getElementById("academicLevel");
    var ferror = [f1,f2,username,password];
    var i;
    if(academicLevel =="Nivel Academico"){
        f3.style.borderColor = "red";
    }
    else{
        if(academicLevel !="Nivel Academico"){
            f3.style.borderColor = "green";
        }
    }
    for (i = 0; i < ferror.length; i++) {
        if(ferror[i].value.length < 1){
            ferror[i].style.borderColor = "red";
        }
        else{
            ferror[i].style.borderColor = "green";
        }
    }
}
