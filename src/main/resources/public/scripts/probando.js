function controladorSlideBar() {
    console.log("Entrando a bloqueador de slidebar...")
    var x1 = document.getElementById("statusMapa")
    var x2 = document.getElementById("statusInforme");
    var x3 = document.getElementById("statusFormulario");
    var x4 = document.getElementById("statusLogin");

    var indicator = [x1,x2,x3,x4];
    var i;
    var boolean = false;
    for (i = 0; i < indicator.length; i++) {
        if(indicator[i].click() == true){
            boolean = true;
        }
        else{
            boolean = false;
        }
    }
    slideBar(boolean);
}

function slideBar(prueba1) {
    console.log("Entrando a switch case...")

    switch (prueba1) {
        case true:
            document.getElementById("statusMapa").removeAttribute("disabled")
            document.getElementById("statusInforme").removeAttribute("disabled")
            document.getElementById("statusFormulario").removeAttribute("disabled")

            break;
        case false:
            document.getElementById("statusMapa").setAttribute("disabled", "true");
            document.getElementById("statusInforme").setAttribute("disabled", "true");
            document.getElementById("statusFormulario").setAttribute("disabled", "true");
            break;
    }
}