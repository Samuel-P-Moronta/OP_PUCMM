package oppucmm.webservices.REST.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import oppucmm.controllers.Controller;
import oppucmm.models.Form;
import oppucmm.models.FormAux;
import oppucmm.models.User;
import oppucmm.services.FormService;
import oppucmm.services.UserService;
import io.jsonwebtoken.security.SignatureException;


import oppucmm.webservices.REST.Client.RESTClient;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;

public class RESTApi {
    private Javalin app;
    private Map<String, Object> model = new HashMap<>();
    public RESTApi(Javalin app) {
        this.app = app;
    }
    public void aplicarRutas() {
        filtro_Cors();
        app.routes(() -> {
            /**
             * Ejemplo de una API REST, implementando el CRUD
             * ir a
             */
            path("/restApi", () -> {
                //Autenticar Usuario
                get("/:autenticar", ctx -> {

                    JWT repuestaLogin = null;
                    System.out.println( "Parametro recibido: " + ctx.pathParam("autenticar"));
                    User usuario = null;
                    usuario = Controller.getInstance().getUserByUsername(ctx.pathParam("autenticar", String.class).get());
                    if(usuario!= null){
                        repuestaLogin = generateJWT(usuario);
                        System.out.println("Enviando repuesta el cliente...");
                        System.out.println("token -> "+repuestaLogin.getToken());
                        ctx.json(repuestaLogin);
                    }else{
                        ctx.json(repuestaLogin);
                    }


                });

                path("/formulario",()->{
                    before("/addForm",ctx -> {
                        //Header for the token
                        String header = "Authorization";
                        String prefix = "Bearer ";

                        String jwt = ctx.header(header);
                        if(jwt == null || !jwt.startsWith(prefix)){
                            throw new ForbiddenResponse("Bad request! You can't access!");
                        }else{
                            jwt = jwt.replace(prefix, "");
                            Claims claims = null;
                            try {
                                claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(RESTController.KEY_SECRET.getBytes())).parseClaimsJws(jwt).getBody();
                                System.out.println("JWT received: " + claims.toString());

                            }catch (ExpiredJwtException | MalformedJwtException | SignatureException e){
                                throw new ForbiddenResponse(e.getMessage());
                            }
                        }
                    });

                    after(ctx -> {
                        ctx.header("Content-Type", "application/json");
                    });
                    //Listar
                    get("/", ctx -> {
                        ctx.json(FormService.getInstance().explorarTodo());
                    });
                    //Agregar
                    post("/addForm", ctx -> {
                        FormAux f = ctx.bodyAsClass(FormAux.class);
                        ctx.json(Services.getInstance().addFormDb(f));
                    });
                });
            });
        });
    }
    /**
     *  Aplicando el filtro para permitir el CORS.
     *  Si estamos realizando una consulta desde un navegador es
     *  necesario dar permisos de intercambio de información.
     */
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
    /**
     * Metodo para la generación de la trama JWT
     * @param u1
     * @return
     */
    private static JWT generateJWT(User u1){
        JWT j1 = new JWT();
        //Generating the key
        SecretKey secretKey = Keys.hmacShaKeyFor(RESTController.KEY_SECRET.getBytes());
        //Valid date
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(30);

        //Generate the JWT (frame)
        String jwt = Jwts.builder()
                .setIssuer("PUCMM-FINAL-PROJECT")
                .setSubject("OP | PUCMM API's")
                .setExpiration(Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4))))
                .claim("username", u1.getUsername())
                .signWith(secretKey)
                .compact();
        j1.setToken(jwt);
        return j1;
    }
}
