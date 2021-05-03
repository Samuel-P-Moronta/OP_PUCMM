package oppucmm.webservices.REST.Client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import io.jsonwebtoken.security.Keys;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import oppucmm.models.*;
import oppucmm.services.UserService;
import oppucmm.webservices.REST.Server.JSONParser;
import oppucmm.webservices.REST.Server.JWT;
import oppucmm.webservices.REST.Server.RESTController;
import oppucmm.webservices.REST.Server.RESTService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RESTClient {
    //private static JWT j1 = null;
    //static String url = "http://localhost:7000/api",urlLogin = "http://localhost:7000/api-rest",token;
    static String url = "https://astrocaribbean.tech/api",urlLogin = "https://astrocaribbean.tech/api-rest",token;
    private Javalin app;
    private Map<String, Object> model = new HashMap<>();

    public RESTClient(Javalin app) {
        this.app = app;
    }

    public static void main(String[] args) {
        Javalin app2 = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        }).start(8043);
       // aplicarRutas(app);

    }


    /* Comunicacion con el servidor...*/
    public static String logIn(String username, String password) {
        //HttpResponse<String> repuestaServidor = Unirest.get(String.valueOf(new String[]{"http://localhost:7000/restApi/" + "{autenticar}"})).routeParam("autenticar", usuario).asString();
        HttpResponse<String> response = Unirest.post(urlLogin+"/login").field("username",username).field("password",password).asString();
        Gson gson = new Gson();
        //j1 = gson.fromJson(repuestaServidor.getBody(), JWT.class);
        //System.out.println("Token recibido... " + j1.getToken());
        /*if (j1.getToken() != null) {
            return true;
        }*/
        return response.getBody();
    }

    public static String listForms() {
        try{
            HttpResponse<String> response = Unirest.get(url+"/formularios").header("Authorization",token).asString();
            System.out.println("Status: " + response.getStatus());
            System.out.println(response.getBody());
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String createForm(Form form) {
        System.out.println("\nSend Parameters to the rest server");

        JsonObject st = new JsonObject();

        st.addProperty("name", form.getName());
        st.addProperty("sector", form.getSector());
        st.addProperty("academicLevel", form.getAcademicLevel());

        JsonObject jsonLocation = new JsonParser().parse(JSONParser.toJson(form.getLocation())).getAsJsonObject();
        st.add("location",jsonLocation);
        JsonObject jsonPhoto = new JsonParser().parse(JSONParser.toJson(form.getPhoto())).getAsJsonObject();
        st.add("photo",jsonPhoto);
        JsonObject jsonUser = new JsonParser().parse(JSONParser.toJson(form.getUser())).getAsJsonObject();
        st.add("user",jsonUser);
        HttpResponse<String> response = Unirest.post(url+"/formularios/crear")
                .header("Content-Type", "application/json").header("Authorization",token).body(st).asString();


        return response.getBody();
    }

    public static void logOutServer(){
        HttpResponse<String> response = Unirest.get(urlLogin+"/logOutRest").asString();
    }



    public void aplicarRutas() {
        //filtro_Cors();
        RESTService restService= new RESTService();


        app.get("/", ctx -> ctx.redirect("/api"));

        app.routes(() -> path("/api", () -> {

            get("/", ctx -> ctx.redirect("api/formularios"));

            get("/formularios", ctx -> {
                if(ctx.sessionAttribute("user") != null){
                    User user = UserService.getInstance().buscar(ctx.sessionAttribute("user"));

                    //List<Form> forms = restService.findFormsByUser(user);
                    //List<String> strings = forms.stream().map(JSONParser::toJson).collect(Collectors.toList());
                    //String strings = JSONParser.toJson(forms);
                    String strings = listForms();

                    if(JSONParser.isJson(strings)) {
                        model.put("formularios", strings);
                        System.out.println(strings);
                        ctx.render("/public/RestTemplate/formRest.html", model);
                    }
                    else {

                        JsonObject st = new JsonObject();
                        ErrorResponse response = new ErrorResponse(strings);
                        st.addProperty("Error",strings);
                        ctx.json(response);
                        //ctx.redirect("/login");
                    }
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
                        String encodedString = "data:image/jpeg;base64,";
                        encodedString += Base64.getEncoder().encodeToString(bytes);
                        Photo foto = new Photo(encodedString);
                        foto.setMimeType(uploadedFile.getContentType());
                        if(ctx.sessionAttribute("user") != null) {
                            User user = UserService.getInstance().buscar(ctx.sessionAttribute("user"));
                            Location location = new Location(ctx.formParam("longitude",Double.class).get(),ctx.formParam("latitude",Double.class).get());
                            Form form2 = new Form(foto, ctx.formParam("fullNameRest"),ctx.formParam("sectorRest"),
                                    ctx.formParam("academicLevelRest"),user,location);

                            form.set(form2);
                        }
                        else {
                            ctx.redirect("/");
                        }

                        //product = productService.find(ctx.pathParam("id", Integer.class).get());

                        //productService.update(product);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //restService.addForm(form.get());
                    createForm(form.get());
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
            logOutServer();
            ctx.redirect("/");
        });



        app.get("/login",ctx -> {
            ctx.render("public/RestTemplate/loginRest.html");
        });

        app.post("/login",ctx -> {

           /* String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            User user = restService.logIn(username,password);
            SecretKey secretKey = Keys.hmacShaKeyFor(RESTController.KEY_SECRET.getBytes());*/
            String request = logIn(ctx.formParam("username"),ctx.formParam("password"));
            JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

            if(jsonObject.get("user") != null){
                //User user = new Gson().fromJson(jsonObject.get("user"),User.class);
                String username = jsonObject.get("user").getAsString();
                User user = UserService.getInstance().buscar(username);
                ctx.sessionAttribute("user", user.getUsername());
                token = "Bearer";
                token += jsonObject.get("token").getAsString();
                ctx.redirect("api/formularios");
            }
            else {
                model.put("Error", "Usuario o contraseña incorrectos!");
                ctx.render("public/RestTemplate/loginRest.html", model);
            }

        });
    }


}
