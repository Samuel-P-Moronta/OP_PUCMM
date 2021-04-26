package oppucmm.webservices.REST.Server;

import io.javalin.Javalin;
import kong.unirest.Client;
import oppucmm.controllers.Controller;
import oppucmm.models.Form;
import oppucmm.models.FormAux;
import oppucmm.models.Photo;
import oppucmm.models.User;
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
                    model.put("formularios",Controller.getInstance().listForm());
                  //  model.put("formularios",RESTClient.listForm(ctx.sessionAttribute("user")));
                    model.put("user", ctx.sessionAttribute("user"));
                    ctx.render("public/RestTemplate/formRest.html", model);
                });
                post("/addForm",ctx -> {
                    Double longitude = ctx.formParam("longitude",Double.class).get();
                    Double latitude = ctx.formParam("latitude",Double.class).get();
                    List<Photo> misFotos = new ArrayList<>();


                    ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                        System.out.println("\n ENTRANDO A METODO PARA CARGAR IMAGENES.... \n");
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            Photo foto = new Photo(encodedString);
                            misFotos.add(foto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    if(misFotos.size() > 0){
                        /*
                        FormAux f1 = new FormAux(ctx.formParam("fullNameRest"),
                                ctx.formParam("sectorRest"),
                                ctx.formParam("academicLevelRest"),longitude,latitude);
                        */
                    }
                    /*
                    if(RESTClient.addForm(f1)== false){
                        System.out.println("no se pudo agregar");
                    }else{
                        System.out.println("Se pudo agregar");
                    }
                    ctx.redirect("/formRest");

                     */
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
