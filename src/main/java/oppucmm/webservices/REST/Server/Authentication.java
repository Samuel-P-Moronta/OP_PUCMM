package oppucmm.webservices.REST.Server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import oppucmm.controllers.Controller;
import oppucmm.models.Form;
import oppucmm.models.User;
import oppucmm.services.UserService;
import org.hibernate.tool.schema.extract.spi.InformationExtractor;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Authentication {
    static final String escryptPassword = "admin";
    public final static String keySecret = "asd12D1234dfr123@#4Fsdcasdd5g78a";

    public static void authentication(){
        get("/login", ctx -> {
            if (ctx.sessionAttribute("user") == null) {
                ctx.render("/loginRest.html");
            }
            ctx.redirect("/");
        });
        post("/login", ctx -> {
            ctx.contentType("application/json");
            String username = ctx.formParam("username","");
            String password = ctx.formParam("username","");
            User u1 = UserService.getInstance().buscar(username);
            if(u1!= null && u1.getPassword().equals(password)){
                //return generateJWT(u1);
            }else{
                System.out.println("No");
            }
        });
    }
    private static JWT generateJWT(User u1){
        JWT j1 = new JWT();
        //Generating the key
        SecretKey secretKey = Keys.hmacShaKeyFor(keySecret.getBytes());
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
       // return new j1;
        return null;
    }
}
