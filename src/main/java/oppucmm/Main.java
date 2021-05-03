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
import oppucmm.webservices.REST.Client.RESTClient;
import oppucmm.webservices.REST.Server.RESTController;
import oppucmm.webservices.SOAP.Server.SOAPController;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;

import java.sql.SQLException;
import java.util.Arrays;

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

        /*Javalin app2 = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        }).start(8043);*/

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
        Form form1 = new Form(new Photo("data:image/jpeg;base64,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 formularios:173:65"),"Juan","Santiago","Grado",auxUsuario,l1);
   //     Form form2 = new Form(new Photo("data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAADSCAMAAABD772dAAAAV1BMVEX///+ZmZmUlJTb29v4+Pi1tbXIyMiampqTk5P8/Py5ubnx8fGdnZ2ioqL6+vrT09Ph4eHo6Oitra2/v7/t7e2mpqaNjY3MzMy3t7ewsLDCwsLQ0NCIiIioxQZlAAAF9UlEQVR4nO2da5ObOgxAY/GyMSS8CeT+/995Q9hus4HwCBYWXZ0P7WxnOuXUtizbwpxODMMwDMMwDMMwDMMwDMO8R+nk4udhT577F6c4234mNNzUab1YVhL+UlVBlt8SrWw/nHlS34sDADHg/mdRFl5c2w9oEqVv8ZjrD/L0X3E+J3ksZ2wfLZ1dtO1nNUHSRPO2vbIo68O3cupVc335QdD/JqNjD2ZdR0t0g+dmbhLbT/0xyokWjN1Bx4ZrYfvJP8Nt19v2yvEhGzkt4aW3LqT7K/7xRnIdLwpW7xrZO9gMpfyPdfs+AdmhBvI539K8vXFwpIG83bcLXaltjaW4ngHfO9VRjFszvkJEx+jVfbz6YD4acoheXX+QXb0DSvrzcWJOtzMOqe8A6dLUAP4ybm0bzRCa9b1DO+WqK8O6AZSUjYvYsK+g3anP5jt0B93Z2GyE/gN4ZCM1Qod+4NgWe0ON0qG7pSLN9ENnOL5CyIttt1FuSA18JyY5ijM8YZKjuDCdczxDMaXGmYO/ILhOxEiy/kIwbF0wG1iIyLbfK8pD9RUVtalYo/boe/KR2zZ8wcH1FcIjVgZyM7iTNQq1OI08hIUgdhDhLi1r+JzatuMPNLowXEkNYvSYJYBW1GqlmaOGCWjtyRs7TpqA0oGxygG9hUmdJbros5IISAnrEl1YVJQmYvxZ6b5CJCWMPYDJCe8QpCWlfS2N70ushX/dGP5tUdptdhCmNA//ukzLSOndDEAplz752Ds8QtA6Qryg+xI7Fv91Ox6/bk9Loc9LxHYtTzeJPC1FpGal08nBDtO0Yhb62VJA7mzpjFbR0lORSjs6sGdi234DUtwKgJttvyGofZra2WFHgRmnr6QS6S8M18I/A/RqWk6oYau07TYK3j6PpJVHf4NVbEn2ZR6F40uz0vJBghKoqZZLn77eeTC+agJiC8NnEoQ1osxJbXW88Hjx0Kg17feW7osm05GacofuKAxPxpRf0+pxjDYxeGQj9DcmC3qID+CeDTdaDKC4KhxybozlH2RTrJ+4VzNtTG0r+j3aM9HGwUHat2NroVqXuhzkTosv9NbzUygP5XuP1bdtvse6e+hBsmGPK8jp5xtD0k+nJwgulBdIEziLrgAc+IYHSK/ekF7FWmUZEzxkWIHTrFKGqD5u8/YoJ1s8lEHWxwvOQ85OuWQsg4j9A7Wumrw7OPUzmG5nGV+dSd2C1ETlhk00eRyi3LSuqvErlwHkf1kyc5O4irOGyFpR6VvZXbg7vzmh/byJokg83SB+/7EM8/lVwuPWqkrWif1KD92WXy9pBQtOgHSaJt93xIdhe0nSJZfEn5u+b8gotJxiu/5TPIKl6zml1OOXpfmUun4X64Kweqdp+nPKQVqx/7wWFGJriaca3D+LsoZVg1uMczuN7OYjM41545FrX6G0kZ+4ozeVRKaLE9TYPwPl/lOUbsazJ/CNzhy6Gbyo2/0Iu28AqXxUtyM0GFPentzsvkXfvs8UoTE1wtTEghqyXY0nT5CgNLPB6vpTB69gsifNMXtGaOKO93Smtm9xnmOA2aMFmSUb///1ZXbnYL9hnMwvbyFuNzVysuCdgt1qqN1ySVUDbPgKSRHOfrynQ+40Gy+tMYTms9Wcrhd+AWSnuOW+STnGaJzVrVz4y78Qsc/58ZoLWUFk6zp2mq/ZywYfS/KZdTUNAKW/8FtZSjtNsG4fe5civdUvoUmR1cnck6n0EsZyjW4XOOUOy6bPrhgOSm8q3dd59tE7E3vMTJ9W6YCs4rx20kJr1z13GzxnV+sidW5tUy377NYr9zYO0H03faYDpIjjMvPy1vf9Nr9mZRlHsKUiNUDPtoodLnRYA/rbEMUOl7KsAf2sMUVo4WDDfyL66xAOsRZusbPL2rbhCyF26uHbNnwBvdyWhS3DwizMwizMwixMCBZmYRZmYRZmYUKwMAuzMAuzMAsTgoVZmIVZmIVZmBAszMIszMIszMKEYGEWZmEWnhaWEuggZYMtXDi02PrKH8MwDMMwDMMwDMMwDHM8/gdrcWzSOvGiKwAAAABJRU5ErkJggg== formularios:173:65"),"Soto Bello","Santiago","Doctorado",auxUsuario,l2);
  //      Form form3 = new Form(new Photo("data:image/jpeg;base64,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 formularios:173:65"),"Dilapa Batista","Santiago","Medio",auxUsuario,l3);
/*
        Form form1 = new Form(new Photo(),"Juan","Santiago","Grado",auxUsuario,l1);
        Form form2 = new Form(new Photo(),"Soto Bello","Santiago","Doctorado",auxUsuario,l2);
        Form form3 = new Form(new Photo(),"Dilapa Batista","Santiago","Medio",auxUsuario,l3);
*/

        Controller.getInstance().addForm(form1);
       /* Controller.getInstance().addForm(form2);
        Controller.getInstance().addForm(form3);*/

        new UserController(app).aplicarRutas();
        new FormController(app).aplicarRutas();
        new WebSocketController(app).aplicarRutas();
       /* new RESTApi(app).aplicarRutas();
        //REST APP
        new RESTController(app2).aplicarRutas();*/
        Javalin app2 = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        }).start(8043);
        new RESTController(app).aplicarRutas();
        new RESTClient(app2).aplicarRutas();



    }
    public static String getModoConexion() {
        return modoConexion;
    }
}