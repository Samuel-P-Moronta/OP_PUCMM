package oppucmm.webservices.REST.Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import oppucmm.models.*;
import oppucmm.services.UserService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RESTController {

    private Javalin app;
    public final static String KEY_SECRET = "asd12D1234dfr123@#4Fsdcasdd5g78a";
    private Map<String, Object> errors = new HashMap<>();

    public RESTController(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
       // filtro_Cors();
        RESTService restService= new RESTService();
        Map<String, Object> model = new HashMap<>();


       // app.get("/", ctx -> ctx.redirect("/api"));

        app.routes(() -> path("/api", () -> {

            /*after(ctx -> {
                ctx.header("Content-Type", "application/json");
            });*/
            String header = "Authorization";
            String prefijo = "Bearer";
            before("/*",ctx -> {
                String headerAutentificacion = ctx.header(header);
                if(headerAutentificacion ==null || !headerAutentificacion.startsWith(prefijo)){
                    ctx.status(403).result("No tiene permiso para acceder al recurso, no enviando header de autorización");
                    //model.put("Error","No tiene permiso para acceder al recurso, no enviando header de autorización");
                    errors.put("403","No tiene permiso para acceder al recurso, no enviando header de autorización");
                    //return;
                }
                if(errors.isEmpty()) {
                    String tramaJwt = headerAutentificacion.replace(prefijo, "");
                    try {

                        Claims claims = Jwts.parser()
                                .setSigningKey(Keys.hmacShaKeyFor(KEY_SECRET.getBytes()))
                                .parseClaimsJws(tramaJwt).getBody();
                        long tiempo = System.currentTimeMillis();
                        //mostrando la información para demostración.

                        System.out.println("Mostrando el JWT recibido: " + claims.toString());
                    } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) { //Excepciones comunes
                        errors.put("403", e.getMessage());
                        ctx.status(403).result(e.getMessage());
                    }
                }

            });

            get("/", ctx -> ctx.redirect("api/formularios"));

            get("/formularios", ctx -> {
                if(errors.isEmpty()) {
                    if (ctx.sessionAttribute("user") != null) {
                        User user = UserService.getInstance().buscar(ctx.sessionAttribute("user"));
                        List<Form> forms = restService.findFormsByUser(user);
                        forms.stream().forEach(form ->
                                form.getPhoto().setFotoBase64(form.getPhoto().getFotoBase64().replace("data:image/jpeg;base64,/9j/",""))

                        );
                        //List<String> strings = forms.stream().map(JSONParser::toJson).collect(Collectors.toList());
                        String strings = JSONParser.toJson(forms);
                        model.put("formularios", strings);
                        //System.out.println(strings);
                        //ctx.render("/public/RestTemplate/formRest.html",model);
                        JsonArray jsonObject = new JsonParser().parse(strings).getAsJsonArray();
                        ctx.json(forms);
                    } else {

                        ctx.redirect("/login");
                    }
                }
                else {

                    ctx.status(403).result(errors.get("403").toString());
                    errors = new HashMap<>();
                }
            });

            post("/formularios/crear", ctx -> {
                var files = ctx.uploadedFiles("fotoR");
                //AtomicReference<Form> form = new AtomicReference<>(new Form());
                Form form = ctx.bodyAsClass(Form.class);
                /*ctx.uploadedFiles("fotoR").forEach(uploadedFile -> {
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
                });*/
                restService.addForm(form);
                //ctx.redirect("/");
                ctx.json(form);

            });





        }));


            app.get("/api-rest/logOutRest", ctx -> {
                if (ctx.sessionAttribute("user") != null) {
                    ctx.sessionAttribute("user", null);
                    ctx.req.getSession().invalidate();
                }
                if (ctx.cookie("user") != null) {
                    ctx.removeCookie("user");
                }
                //ctx.redirect("/");
                ctx.status(200);
            });



        /*app.get("/api/login",ctx -> {
            ctx.render("public/RestTemplate/loginRest.html");
        });*/

        app.post("/api-rest/login",ctx -> {

            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            User user = restService.logIn(username,password);
            SecretKey secretKey = Keys.hmacShaKeyFor(RESTController.KEY_SECRET.getBytes());

            if(user != null){

                ctx.sessionAttribute("user", username);

                //ctx.redirect("api/formularios");

                ctx.json(generateJWT(user));
            }
            else {
                model.put("Error", "Bad Credentials");
                ctx.json(model);
               // ctx.render("public/RestTemplate/loginRest.html", model);
            }

        });


    }

    private void filtro_Cors(){
        //  Filtro para validar el CORS.(Intercambio de recursos de origen cruzado).
        app.before("/*", ctx ->{
            ctx.header("Access-Control-Allow-Origin", "*");
        });
        // Enviando la información a solicitud del CORS.
        app.options("/*", ctx -> {
            System.out.println("Accediendo al metodo de options");

            String accessControlRequestHeaders = ctx.header("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                ctx.header("Access-Control-Allow-Headers",accessControlRequestHeaders);
            }

            String accessControlRequestMethod = ctx.header("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                ctx.header("Access-Control-Allow-Methods",accessControlRequestMethod);
            }
            ctx.status(200).result("OK");
        });
    }

    private static JWT generateJWT(User u1){
        JWT j1 = new JWT();
        //Generating the key
        SecretKey secretKey = Keys.hmacShaKeyFor(KEY_SECRET.getBytes());
        //Valid date
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);
        long tiempo = System.currentTimeMillis();
        //Generate the JWT (frame)
        String jwt = Jwts.builder()
                .setIssuer("PUCMM-FINAL-PROJECT")
                .setSubject("OP | PUCMM API's")
                .setIssuedAt(new Date())
                .setExpiration(new Date(tiempo+120000))
                //.setExpiration(Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4))))
                .claim("username", u1.getUsername())
                .signWith(secretKey)
                .compact();
        j1.setToken(jwt);
        j1.setUser(u1.getUsername());
        JsonObject st = new JsonObject();
    /*JsonObject jsonJWT = new JsonParser().parse(JSONParser.toJson(j1)).getAsJsonObject();
    st.add("token",jsonJWT);
    st.addProperty("user",u1.getUsername());*/
        return j1;
    }
}
