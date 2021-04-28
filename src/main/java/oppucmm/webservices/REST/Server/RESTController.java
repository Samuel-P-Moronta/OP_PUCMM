package oppucmm.webservices.REST.Server;

import io.javalin.Javalin;
import oppucmm.controllers.Controller;
import oppucmm.models.*;
import oppucmm.services.FormService;
import oppucmm.services.UserService;
import oppucmm.webservices.REST.Client.RESTClient;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.IOException;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RESTController {
    public final static String KEY_SECRET = "asd12D1234dfr123@#4Fsdcasdd5g78a";
    public final static int UNAUTHORIZED = 401;
    public final static int NOT_FOUND = 404;
    private static String mpCryptoPassword = "BornToFight";
    private FormService formService = FormService.getInstance();


    static String URI = "";
    private Javalin app;
    private Map<String, Object> model = new HashMap<>();

    public RESTController(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
        app.get("/", ctx -> ctx.redirect("/formRest"));
        app.routes(() -> {
            path("/formRest", () -> {
                before("/", ctx -> {
                    if (ctx.sessionAttribute("user") == null) {
                        ctx.redirect("/loginRest");
                    }
                });
                get("/", ctx -> {
                    model.put("formularios",RESTClient.listForm());
                 //   model.put("formularios",Controller.getInstance().listForm());
                    ctx.render("public/RestTemplate/formRest.html", model);
                });
                post("/addForm",ctx -> {
                    FormAux form = new FormAux(ctx.formParam("fullNameRest"),
                            ctx.formParam("sectorRest"),
                            ctx.formParam("academicLevelRest"),
                            ctx.formParam("latitude",Double.class).get(),
                            ctx.formParam("longitude",Double.class).get()
                    );
                    RESTClient.addForm(form);

                    ctx.redirect("/formRest");


                });
            });
            path("/logOutRest", () -> {
                get("/", ctx -> {
                    if (ctx.sessionAttribute("user") != null) {
                        ctx.sessionAttribute("user", null);
                        ctx.req.getSession().invalidate();
                    }
                    if (ctx.cookie("user") != null) {
                        ctx.removeCookie("user");
                    }
                    ctx.redirect("/");
                });

            });
        });
        app.post("/loginRest", ctx -> {
            String rememberMe = "";
            Map<String, Object> modelo = new HashMap<>();
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            //Authenticator
            User aux = UserService.getInstance().buscar(username);
            if (aux!=null) {
                if (aux.getPassword().equals(password)) {
                    rememberMe = ctx.formParam("rememberMe");
                    if (rememberMe != null) {
                        if (rememberMe.equalsIgnoreCase("ON")) {
                            System.out.println("Creando cookie...\n");
                            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                            encryptor.setPassword(mpCryptoPassword);
                            encryptor.encrypt(aux.getPassword());
                            ctx.cookie("username", aux.getUsername(), 604800);
                            ctx.cookie("password", encryptor.encrypt(aux.getPassword()), 604800);
                        } else {
                            System.out.println("Cookie no pudo ser creada...\n");
                        }
                    }
                    System.out.println("username " + aux.getUsername() + " entered to system [REST-CLIENT]");
                    ctx.sessionAttribute("user", username);
                    ctx.redirect("/formRest");
                } else {
                    System.out.println("User: " + aux.getUsername() + " entered an incorrect password");
                    modelo.put("Error", "Contraseña incorrecta!");
                }
            }
            ctx.render("public/RestTemplate/loginRest.html", modelo);

        });
        app.get("/loginRest", ctx -> {
            ctx.render("public/RestTemplate/loginRest.html");
        });
    }

}
