
window.addEventListener('online',  updateOnlineStatus);
window.addEventListener('offline', updateOnlineStatus);

function updateOnlineStatus(event) {
    var condition = navigator.onLine ? "online" : "offline";
    document.body.className = condition;
    networkIndicator(condition);
}
function networkIndicator(condition) {

    switch (condition) {
        case "online":
            document.getElementById("enviarForm").removeAttribute("disabled")
            //document.getElementById("enviarForm").setAttribute("disabled", "false");
            var signal = document.getElementById("idIndicator");
            var boton = document.getElementById("enviarForm");
            signal.innerHTML = "Online <i id=\"logo\" class=\"fa fa-check-circle fa-fw\"></i>";
            signal.setAttribute("class", "btn-success");
            break;
        case "offline":
            document.getElementById("enviarForm").setAttribute("disabled", "true");
            var signal = document.getElementById("idIndicator");
            var boton = document.getElementById("enviarForm");
            boton.addEventListener("click",myf1)


            signal.innerHTML = "Offline <i id=\"logo\" class=\"fa fa-times-circle fa-fw\"></i>";
            signal.setAttribute("class", "btn-danger");
            break;
    }
}
function myf1() {
    document.getElementById("enviarForm").style.backgroundColor= "red";
}

function myf2() {
    document.getElementById("enviarForm").style.backgroundColor = "blue";
}

