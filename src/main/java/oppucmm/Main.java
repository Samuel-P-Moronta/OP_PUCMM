package oppucmm;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import oppucmm.controllers.FormController;
import oppucmm.controllers.UserController;
import oppucmm.controllers.Controller;
import oppucmm.controllers.WebSocketController;
import oppucmm.models.Form;
import oppucmm.models.Location;
import oppucmm.models.Photo;
import oppucmm.models.User;
import oppucmm.services.connect.DataBaseServices;
import oppucmm.webservices.REST.Server.RESTController;
import oppucmm.webservices.SOAP.Server.SOAPController;

import java.sql.SQLException;

public class Main {
    private static String modoConexion = "";
    public static void main(String[] args) throws SQLException {
        String msg = "Software ORM - JPA";
        System.out.println(msg);
        if(args.length >=1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }
        if(modoConexion.isEmpty()){
            //Database init
            DataBaseServices.getInstance().InciarBD();
        }
        //Creando la instancia del servidor.
        /*Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/public");
            config.enableCorsForAllOrigins();
        }).start(7000);*/

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();

            JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        });

        Javalin app2 = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        }).start(8043);

        //El contexto SOAP debe estar creando antes de inicio del servidor.
        new SOAPController(app).aplicarRutas();
        app.start(7000);
        //Create fake user
        User auxUsuario = Controller.getInstance().createFakeUser();
        //Create fake forms
      //  Controller.getInstance().createFakeForm();
        // Ubicaciones de prueba
        Location l1 = new Location( -70.663414, 19.453105);
        Location l2 = new Location( -70.644531, 19.453105);
        Location l3 = new Location( -70.664787, 19.473174);

       // Formularios de prueba
        Form form1 = new Form(new Photo(),"Juan","Santiago","Grado",auxUsuario,l1);
        Form form2 = new Form(new Photo(),"Soto Bello","Santiago","Doctorado",auxUsuario,l2);
        Form form3 = new Form(new Photo(),"Dilapa Batista","Santiago","Medio",auxUsuario,l3);

        Controller.getInstance().addForm(form1);
        Controller.getInstance().addForm(form2);
        Controller.getInstance().addForm(form3);

        new UserController(app).aplicarRutas();
        new FormController(app).aplicarRutas();
        new WebSocketController(app).aplicarRutas();
       /* new RESTApi(app).aplicarRutas();
        //REST APP
        new RESTController(app2).aplicarRutas();*/

        new RESTController(app2).aplicarRutas();



    }
    public static String getModoConexion() {
        return modoConexion;
    }
}