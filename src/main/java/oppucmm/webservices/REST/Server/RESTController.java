package oppucmm.webservices.REST.Server;

import io.javalin.Javalin;
import oppucmm.models.*;
import oppucmm.services.UserService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RESTController {

    private Javalin app;

    public RESTController(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {

        RESTService restService= new RESTService();
        Map<String, Object> model = new HashMap<>();

        app.get("/", ctx -> ctx.redirect("/api"));

        app.routes(() -> path("/api", () -> {

            get("/", ctx -> ctx.redirect("api/formularios"));

            get("/formularios", ctx -> {
                if(ctx.sessionAttribute("user") != null){
                    User user = UserService.getInstance().buscar(ctx.sessionAttribute("user"));
                    List<Form> forms = restService.findFormsByUser(user);
                    //List<String> strings = forms.stream().map(JSONParser::toJson).collect(Collectors.toList());
                    String strings = JSONParser.toJson(forms);
                    model.put("formularios",strings);
                    System.out.println(strings);
                    ctx.render("/public/RestTemplate/formRest.html",model);
                    //ctx.json(strings);
                }
                else{

                    ctx.redirect("/login");
                }
            });

            post("/formularios", ctx -> {
                var files = ctx.uploadedFiles("fotoR");
                AtomicReference<Form> form = new AtomicReference<>(new Form());
                ctx.uploadedFiles("fotoR").forEach(uploadedFile -> {
                    try {

                        byte[] bytes = uploadedFile.getContent().readAllBytes();
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        Photo foto = new Photo(encodedString);
                        foto.setMimeType(uploadedFile.getContentType());
                        User user = UserService.getInstance().buscar(ctx.sessionAttribute("user"));

                        //product = productService.find(ctx.pathParam("id", Integer.class).get());

                        //productService.update(product);
                        Location location = new Location(ctx.formParam("longitude",Double.class).get(),ctx.formParam("latitude",Double.class).get());
                      Form form2 = new Form(foto, ctx.formParam("fullNameRest"),ctx.formParam("sectorRest"),
                                ctx.formParam("academicLevelRest"),user,location);

                      form.set(form2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    restService.addForm(form.get());
                });

                ctx.redirect("/");


            });





        }));


            app.get("/logOutRest", ctx -> {
                if (ctx.sessionAttribute("user") != null) {
                    ctx.sessionAttribute("user", null);
                    ctx.req.getSession().invalidate();
                }
                if (ctx.cookie("user") != null) {
                    ctx.removeCookie("user");
                }
                ctx.redirect("/");
            });



        app.get("/login",ctx -> {
            ctx.render("public/RestTemplate/loginRest.html");
        });

        app.post("login",ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            User user = restService.logIn(username,password);
            if(user != null){
                ctx.sessionAttribute("user", username);
                ctx.redirect("api/formularios");
            }
            else {
                model.put("Error", "Contraseña incorrecta!");
                ctx.render("public/RestTemplate/loginRest.html", model);
            }

        });


    }
}
